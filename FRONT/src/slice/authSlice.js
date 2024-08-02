import { checkEmail } from "../api/auth";

/*
우선 이메일 중복처리만 구현했는데 위에 api 요청 함수를 가져왔고
그걸 duplicateCheckEmail 함수에 썼어 아까랑 똑같이 여기엔 email이 없자너 결국 데이터가 입력받는 곳에 있으니까
또 다시 인자를 열어서 checkEmail에 연결해주면 됭
그 다음 isDuplicatedEmail을 갱신해주면 될거야 result.isDuplicate라고 쓴 건 api 명세 보면 알듯
이제 이걸 직접 쓴 SignUpForm으로 ㄱㄱ
*/

export const createAuthSlice = (set) => ({
  isLogin: true,
  isDuplicatedEmail: false,
  duplicateCheckEmail: async (email) => {
    const result = await checkEmail(email);
    set({ isDuplicatedEmail: result.isDuplicate });
  },
});
