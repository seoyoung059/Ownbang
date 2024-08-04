import { checkEmail, checkPhoneNumber, signUp, login } from "../api/auth";
import { Cookies } from "react-cookie";
import { toast } from "react-toastify";

/*
우선 이메일 중복처리만 구현했는데 위에 api 요청 함수를 가져왔고
그걸 duplicateCheckEmail 함수에 썼어 아까랑 똑같이 여기엔 email이 없자너 결국 데이터가 입력받는 곳에 있으니까
또 다시 인자를 열어서 checkEmail에 연결해주면 됭
그 다음 isDuplicatedEmail을 갱신해주면 될거야 result.isDuplicate라고 쓴 건 api 명세 보면 알듯
이제 이걸 직접 쓴 SignUpForm으로 ㄱㄱ
*/

const cookies = new Cookies();

export const createAuthSlice = (set) => ({
  isAuthenticated: false, // 초기 상태 설정
  // 중복 확인 BOOLEAN
  isDuplicatedEmail: false,
  isDuplicatedPhoneNumber: false,

  duplicateCheckEmail: async (email) => {
    const result = await checkEmail(email);
    set({ isDuplicatedEmail: result.data.isDuplicate });
  },

  duplicateCheckPhoneNumber: async (phoneNumber) => {
    const result = await checkPhoneNumber(phoneNumber);
    set({ isDuplicatedPhoneNumber: result.data.isDuplicate });
  },

  makeUser: async (userData) => {
    const result = await signUp(userData);
    return result.resultCode;
  },

  loginUser: async (loginData) => {
    const result = await login(loginData);
    localStorage.setItem("accessToken", result.data.accessToken);
    cookies.set("refreshToken", result.data.refreshToken);
    set({ isAuthenticated: true });
    return result;
  },

  isLogin: () => {
    return localStorage.getItem("accessToken");
  },

  logout: () => {
    localStorage.removeItem("accessToken");
    set({ isAuthenticated: false });
    toast.success("로그아웃 완료되었습니다.", {
      position: "bottom-left",
      autoClose: 2000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
    });
  },
});
