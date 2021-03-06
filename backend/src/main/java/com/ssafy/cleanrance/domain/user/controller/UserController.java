package com.ssafy.cleanrance.domain.user.controller;

import com.ssafy.cleanrance.domain.user.db.entity.User;
import com.ssafy.cleanrance.domain.user.request.AuthRequest;
import com.ssafy.cleanrance.domain.user.request.StoreSignUpRequest;
import com.ssafy.cleanrance.domain.user.request.UserPutRequest;
import com.ssafy.cleanrance.domain.user.request.UserSignUpRequest;
import com.ssafy.cleanrance.domain.user.service.UserService;
import com.ssafy.cleanrance.global.util.JwtTokenUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;
    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;


    @PostMapping(value = "/signup/store",consumes = {"multipart/form-data"})
    @ApiOperation(value = "매장 회원가입", notes = "매장 회원가입을 진행한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity signupStore(
            @RequestPart(value = "storeSignUpRequest") StoreSignUpRequest storeSignUpRequest,
            @RequestPart(value = "file", required = false) MultipartFile image) throws IOException {
        String str =userService.createStore(storeSignUpRequest,image);
        if("OK".equals(str)){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }


    @PostMapping("/signup/user")
    @ApiOperation(value = "구매자 회원가입", notes = "구매자 회원가입을 진행한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity signupUser(
            @RequestPart UserSignUpRequest userSignUpRequest,
            @RequestPart(value = "file",required = false) MultipartFile image) throws IOException {
        String str = userService.createUser(userSignUpRequest, image);
        if("OK".equals(str)){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/member")
    @ApiOperation(value = "회원 본인 정보 조회", notes = "회원 본인의 정보를 응답한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<Optional<User>> findUser(@RequestParam String userId){
        Optional<User> user = userService.findById(userId);
        return ResponseEntity.status(200).body(user);
    }

    @PutMapping("/member")
    @ApiOperation(value = "회원 본인 정보 수정", notes = "회원 정보(이름,이메일,전화번호, 주소 수정)를 수정하여 저장한다.<br/>")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<Optional<User>> updateUser(@RequestBody UserPutRequest user){
        Optional<User> update = Optional.ofNullable(userService.updateUser(user).orElse(null));
        if(null == update){
            throw new IllegalStateException("없는 아이디");
        }
        return ResponseEntity.status(200).body(update);
    }

    @DeleteMapping("/member")
    @ApiOperation(value = "회원삭제", notes = "회원을 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity delete(@RequestParam String userId){
        userService.deleteUser(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    @ApiOperation(value = "로그인", notes = "<strong>아이디와 비밀번호</strong>를 통해 로그인한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        Optional<User> user = userService.findById(authRequest.getUser_id());
        String user_id = authRequest.getUser_id();
        if(null != user){
            if(!passwordEncoder.matches(authRequest.getUser_password(), user.get().getUserPassword())){
                throw new IllegalStateException("잘못된 비밀번호입니다.");
            }
            String[] arr = {jwtTokenUtil.getToken(user.get().getUserId()), user_id};
            return ResponseEntity.status(200).body(arr);
        }
        throw new IllegalStateException("잘못된 정보입니다.");
    }

}
