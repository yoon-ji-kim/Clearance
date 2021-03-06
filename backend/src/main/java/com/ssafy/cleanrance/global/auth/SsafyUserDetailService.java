package com.ssafy.cleanrance.global.auth;

import com.ssafy.cleanrance.domain.user.db.entity.User;
import com.ssafy.cleanrance.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 현재 액세스 토큰으로 부터 인증된 유저의 상세정보(활성화 여부, 만료, 롤 등) 관련 서비스 정의.
 */
@Component
public class SsafyUserDetailService implements UserDetailsService {
	@Autowired
	UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<User> user = userService.findById(userId);
    		if(user != null) {
    			SsafyUserDetails ssafyuserDetails = new SsafyUserDetails(user.get());
    			return ssafyuserDetails;
    		}
    		return null;
    }
}
