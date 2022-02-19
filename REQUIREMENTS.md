# 요구사항

## 0~1단계 요구사항
* [ ] 패키지 구조 리팩터링(선택)
  * 뼈대 코드의 구조를 자신이 편한 구조로 리팩터링 하세요.
  * subway는 2주차까지의 미션의 샘플 코드 입니다.
  * auth는 인증과 관련된 로직입니다
  * member는 회원 관리 관련된 로직 입니다.
* [ ] MemberAcceptanceTest의 인수 테스트 통합하기
  * 인수 테스트를 통합하는 경험을 하기 위한 요구사항입니다.
  * 아래의 인수 조건에 맞게 인수 테스트를 리팩터링 하세요.
* [ ] AuthAcceptanceTest의 myInfoWithSession 테스트 메서드를 성공 시키기
  * 뼈대 코드 분석을 위한 요구사항입니다.
  * GET /members/me 요청을 처리하는 컨트롤러 메서드를 완성하여 myInfoWithSession 인수 테스트를 성공시키세요
  * MemberController의 findMemberOfMine메서드를 구현하면 위의 요청을 처리할 수 있습니다.
  * Controller에서 로그인 정보 받아오기
    * SecurityContextHolder에 저장된 SecurityContext를 통해 Authentication 객체 조회
    * Authentication에 저장된 LoginMember의 정보로 존재하는 멤버인지 확인 후 멤버 정보 조회

* [ ] AuthAcceptanceTest의 myInfoWithBearerAuth 테스트 메서드를 성공 시키기
  * [ ] TokenAuthenticationInterceptor 구현하기
    * TokenAuthenticationInterceptorTest 단위 테스트를 작성하며 TDD로 기능 구현하기
* [ ] MemberAcceptanceTest의 manageMyInfo 성공 시키기
  * /members/me 로 멤버 조회/수정/삭제 기능을 요청하는 인수 테스트 작성
  * 로그인 후 token을 응답 받은 후 요청 시 포함시키기
  * [ ] @AuthenticationPrincipal을 활용하여 로그인 정보 받아오기
    * Controller에서 LoginMember 정보를 받아올 때 @AuthenticationPrincipal를 활용하여 받기
    * AuthenticationPrincipalArgumentResolver를 참고하여 ArgumentResolver 기능을 사용하기