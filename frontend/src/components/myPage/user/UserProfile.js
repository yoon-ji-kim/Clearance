import React, { useEffect, useState } from 'react';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Spinner } from 'react-bootstrap';
import Container from 'react-bootstrap/Container';
import NavBar from '../../common/NavBar';
import NavStore from '../../../store/NavStore';
import { Navigate, useNavigate } from 'react-router-dom'
import userStore from '../../../store/userStore';
import axios from 'axios';
import BackButton from '../../BackButton';
import Swal from 'sweetalert2';

function UserProfile(props) {

    const setNavHeader = NavStore(state => state.setNavHeader);
    

    const [userId, setUserId] = useState("");
    const [userName, setUserName] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [userPhone, setUserPhone] = useState("");
    const [userAddress, setUserAddress] = useState("");
    const [userImage, setUserImage] = useState("");
    const [image, setImage] = useState("");

    const [userNameError, setUserNameError] = useState();
    const [emailError, setEmailError] = useState();
    const [phoneError, setPhoneError] = useState();
    const [addressError, setAddressError] = useState();

    const onChangeUserName = (e) => {
        setUserNameError(false);
        setUserName(e.target.value)
    };
    const onChangeEmail = (e) => {
        const emailRegex = /^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$/;
        if (!e.target.value || emailRegex.test(e.target.value)) setEmailError(false);
        else setEmailError(true);
        setUserEmail(e.target.value);
    };
    const onChangePhone = (e) => {
        const phoneRegex = /^[0-9]{3}[-]{1}[0-9]{3,4}[-]{1}[0-9]{4}$/;

        if ((!e.target.value || (phoneRegex.test(e.target.value)))) setPhoneError(false);
        else setPhoneError(true);
        setUserPhone(e.target.value)
    };
    const onChangeAddress = (e) => {
        setAddressError(false);
        setUserAddress(e.target.value)
    };

    const validation = () => {
        if (!userName) setUserNameError(true);
        if (!userEmail) setEmailError(true);
        if (!userPhone) setPhoneError(true);
        if (!userAddress) setAddressError(true);

        if (userName.length === 0 || userEmail.length === 0 || userPhone.length === 0 || userAddress.length === 0 || userNameError || emailError || phoneError || addressError) return true;
        else return false;
    };


    useEffect(() => {
        setNavHeader("?????????");
        // ! axios get
        axios
            .get(`https://k6e203.p.ssafy.io:8443/api/member?userId=${localStorage.getItem("id")}`)
            .then((result) => {
                setUserId(result.data.userId);
                setUserName(result.data.userName);
                setUserEmail(result.data.userEmail);
                setUserPhone(result.data.userPhone);
                setUserAddress(result.data.userAddress);
                setUserImage(result.data.userImage);
            })
            .catch((e) => {
                console.error(e)
            });
    }, []);

    const onSubmit = (e) => {
        if (validation()) return;

        // ! axios POST
        axios
            .put("https://k6e203.p.ssafy.io:8443/api/member",
                {
                    user_address: userAddress,
                    user_email: userEmail,
                    user_id: userId,
                    user_name: userName,
                    user_phone: userPhone,
                }
                ,
                {
                    headers: { 'Content-Type': 'application/json' }
                },
            )
            .then(() => {
                Swal.fire({
                    icon: 'success',
                    title: '???????????? ??????!',
                    showConfirmButton: false,
                    timer: 1500
                  })
                navigate(-1);

            })
            .catch((e) => {
                console.error(e);
            });
    };



    const [loaded, setLoaded] = useState(false);
    const saveImage = (e) => {
        e.preventDefault();
        const fileReader = new FileReader();

        if (e.target.files[0]) {
            setLoaded("loading")
            fileReader.readAsDataURL(e.target.files[0])
        }
        fileReader.onload = () => {
            setImage(e.target.files[0]);
            setUserImage(fileReader.result);
            setLoaded(true);
        }
    }
    const deleteImage = () => {
        setImage("");
        setUserImage("img/default_image.png");
        setLoaded(false);
    }

    let navigate = useNavigate();

    return (
        <div className='userProfile'>
            <NavBar></NavBar>
            <Container className='mt-5 mb-5'>
                <Form>
                    <Form.Group as={Row} className="mb-5" controlId="formFile">
                        <div className='imageDiv'>
                            {loaded === false || loaded === true ?
                                (<img className='imgFile' src={userImage} alt="userImage" />) :
                                (<Spinner animation="border" variant="warning" />)}
                        </div>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3">
                        <Col >
                            <Row sm className='label'>?????????</Row>
                        </Col>
                        <Col sm>
                            <Form.Control maxLength={20} placeholder="?????????" value={userId} disabled readOnly />
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3">
                        <Col >
                            <Row sm className='label'>??????(?????????)</Row>
                        </Col>
                        <Col sm>
                            <Form.Control maxLength={20} placeholder="??????" value={userName} onChange={onChangeUserName} />
                            {userNameError && <div className="invalid-input">???????????? ???????????????.</div>}
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3">
                        <Col >
                            <Row sm className='label'>?????????</Row>
                        </Col>
                        <Col sm>
                            <Form.Control maxLength={50} type="input" placeholder="?????????" value={userEmail} onChange={onChangeEmail} />
                            {emailError && <div className="invalid-input">????????? ????????? ????????? ???????????????.</div>}
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3">
                        <Col >
                            <Row sm className='label'>????????????</Row>
                        </Col>
                        <Col sm>
                            <Form.Control maxLength={20} placeholder="?????????" value={userPhone} onChange={onChangePhone} />
                            {phoneError && <div className="invalid-input">????????? ??????????????? ???????????????. (012-3456-7890)</div>}
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="mb-3">
                        <Col >
                            <Row sm className='label'>??????</Row>
                        </Col>
                        <Col sm>
                            <Form.Control maxLength={20} placeholder="??????" value={userAddress} onChange={onChangeAddress} />
                            {addressError && <div className="invalid-input">????????? ??????????????????. <br /> ?????? ????????? ??????????????? 2 ?????? ???????????? ????????????...</div>}
                        </Col>
                    </Form.Group>

                    <div className="d-grid gap-1 mb-3">
                        <Button className='submit' variant="secondary" onClick={onSubmit}>?????? ?????? ??????</Button>
                    </div>
                </Form>
            </Container>
            <BackButton></BackButton>
        </div>
    );
}

export default UserProfile;