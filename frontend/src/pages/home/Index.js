import axios from 'axios';

export const Index = () => {

  axios({
    method: 'post',
    url: '/auth/signup',
    data: {
      "name": "김은식",
      "nickname": "gamrom",
      "email": "gamrom0@naver.com",
      "password": 123456,
      "phone": "010-1234-5555",
      "university": "국민대학교"
    }
  }).then(function (response) {
    console.log(response.data);
    console.log("성공");
  }).catch(function (error) {
    console.log("실패");
    console.log(error);
  })


  // axios("/auth/signup", {
  //   "name": "김은식",
  //   "nickname": "gamrom",
  //   "email": "gamrom0@naver.com",
  //   "password": 123456,
  //   "phone": "010-1234-5555",
  //   "university": "국민대학교"
  // })
  //   .then(function (response) {
  //     console.log(response.data);
  //     console.log("성공");
  //   }).catch(function (error) {
  //     console.log("실패");
  //     console.log(error);
  //   })

  return (
    <div>이곳은 Home 메인 페이지 입니다.</div>
  );
}

//code => 1 정상적 처리
//

