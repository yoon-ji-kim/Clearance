import React, { useState } from 'react'
import QrReader from 'modern-react-qr-reader'
import { useNavigate } from 'react-router-dom'
import marketStore from '../../../store/marketStore';
import BackButton from '../../BackButton';

function QrCheck() {

  const bs = marketStore(state => state.bookSet)
  const setBS = marketStore(state => state.setBookSet)
  const [state, setState] = useState('No result');

  let urls = "No Result"

  function handleScan(data) {
    if (data !== null && data !== 'No result') {
      urls = data; // bookset이 와야함.
      let result = ''
      const numRegix = /^[0-9]$/;
      for (let i = urls.length - 1; i >= 0; i--) {
        if (numRegix.test(urls.charAt(i)))
          result = urls.charAt(i) + result
        else break;
      }
      localStorage.setItem("bookSet", result)
      navigate("/checkReservation");
    }
  }
  const handleError = err => {
    console.error(err)
  }
  let navigate = useNavigate();
  return (
    <div className='QrCheck'>
      <div className='qrDiv'>
        <div style={{ fontSize: "20px" }}> 아래 카메라에 QR코드를 인식시켜주세요!</div>
        <QrReader
          delay={300}
          facingMode={"environment"}
          onError={handleError}
          onScan={handleScan}
        />
        <p>{state.result}</p>
      </div>
      <BackButton></BackButton>
    </div>
  )
}

export default QrCheck;