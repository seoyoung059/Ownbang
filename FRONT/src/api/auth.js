import Axios from "./index";

/*
api 명세 보면 대분류(prefix) 기준으로 나눠져 있으니 이 기준으로 요청 파일 구분할 거야
여기는 회원가입 / 로그인 요청이 들어가겠지
우선 맨 위에 import 보면 index.js에서 default로 export 했기 때문에 중괄호({})를 쓰지 않았고
Axios든 axios든 뭐라고 쓰든 default로 export한건 그것만 감지하기 때문에 뭐라고 쓰던 상관없지만
axios는 원래 쓰던 메서드랑 이름이 겹칠 수 있으니 그냥 Axios로 통일해서 import 하자

*/

/*
우선 이메일 중복 확인인데 parameter로 이메일을 실어서 get 요청을 보내는 걸 swagger나 노션 api 명세를 읽으면
알 수 있을 거임 모르면 백한테도 물어봐도 되고, 근데 email을 가져와서 담아줘야 하는데 일단 이 파일에 email을 땡겨올 순 없자너?
그래서 이 함수의 인자로 열어서 넣어준거야   async() <- 안에 
그 다음에 response 변수를 열어서 get요청을 담아주면 그 응답이 담기겠쥐
중간에 console.log 도 하면서 잘 넘어오는지 어떻게 넘어왔는지 확인해봐도 좋음
`${}` 이건 알지 백틱에 데이터 넣어서 문자열에 넣는거
그 다음에 이 응답을 그냥 return 하면 대 그 다음에 이제 zustand authSlice로 가봥
*/
export const checkEmail = async (email) => {
  const response = await Axios.get(`/auths/duplicates/email?email=${email}`);
  console.log(response);
  return response.data;
};
