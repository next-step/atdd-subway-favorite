# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 요구사항
### STEP 1
- [x] 토큰 생성
- [x] 토큰을 이용하여 내 정보 조회

### STEP 2
- [ ] 깃허브를 이용한 로그인 구현(토큰 발행)  
  - [ ] 가입되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행하도록
- [ ] 깃허브 로그인 검증 인수 테스트 구현  

--- 
* Github OAuth 과정
1. client_id를 이용하여 다음 url로 GET 요청
2. `https://github.com/login/oauth/authorize?client_id={client_id}`
3. 2번의 url로 접근하면 github 로그인 페이지로 이동, 로그인하면 code와 함께 callback url로 리다이렉션
4. 3에서 얻게된 code를 access token과 교환해야함.
5. `POST https//github.com/login/oauth/access_token` 으로 `client_id`, `client_secret`, `code`를 전달하면 access token을 얻을 수 있음.
6. 5에서 얻은 access token을 이용하여 `GET https://api.github.com/user`로 요청을 보내면 github 유저 정보를 얻을 수 있음.
