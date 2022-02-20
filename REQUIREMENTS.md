# 요구사항

## 0~1단계 요구사항
* [X] 패키지 구조 리팩터링(선택)
  * 뼈대 코드의 구조를 자신이 편한 구조로 리팩터링 하세요.
  * subway는 2주차까지의 미션의 샘플 코드 입니다.
  * auth는 인증과 관련된 로직입니다
  * member는 회원 관리 관련된 로직 입니다.
* [X] MemberAcceptanceTest의 인수 테스트 통합하기
  * 인수 테스트를 통합하는 경험을 하기 위한 요구사항입니다.
* [X] AuthAcceptanceTest의 myInfoWithSession 테스트 메서드를 성공 시키기
  * 뼈대 코드 분석을 위한 요구사항입니다.
  * GET /members/me 요청을 처리하는 컨트롤러 메서드를 완성하여 myInfoWithSession 인수 테스트를 성공시키세요
  * MemberController의 findMemberOfMine메서드를 구현하면 위의 요청을 처리할 수 있습니다.
  * Controller에서 로그인 정보 받아오기
    * SecurityContextHolder에 저장된 SecurityContext를 통해 Authentication 객체 조회
    * Authentication에 저장된 LoginMember의 정보로 존재하는 멤버인지 확인 후 멤버 정보 조회

* [X] AuthAcceptanceTest의 myInfoWithBearerAuth 테스트 메서드를 성공 시키기
  * [X] TokenAuthenticationInterceptor 구현하기
    * TokenAuthenticationInterceptorTest 단위 테스트를 작성하며 TDD로 기능 구현하기
* [X] MemberAcceptanceTest의 manageMyInfo 성공 시키기
  * /members/me 로 멤버 조회/수정/삭제 기능을 요청하는 인수 테스트 작성
  * 로그인 후 token을 응답 받은 후 요청 시 포함시키기
  * [X] @AuthenticationPrincipal을 활용하여 로그인 정보 받아오기
    * Controller에서 LoginMember 정보를 받아올 때 @AuthenticationPrincipal를 활용하여 받기
    * AuthenticationPrincipalArgumentResolver를 참고하여 ArgumentResolver 기능을 사용하기

## 2단계 요구사항
* [ ] 인증 로직 리팩터링 및 기능 추가
  * [ ] 0, 1단계에서 구현한 인증 로직에 대한 리팩터링을 진행하세요
  * [ ] 내 정보 수정 / 삭제 기능을 처리하는 기능을 구현하세요.
  * [ ] Controller 에서 @애너테이션을 활용하여 Login 정보에 접근

### 2단계 힌트
#### AuthenticationConverter 추상화
* [X] 완료 여부
* Token Auth와 FormLogin으로 나뉘어 있는 AuthenticationConverter를 추상화
```
public interface AuthenticationConverter {
AuthenticationToken convert(HttpServletRequest request);
}
```
#### AuthenticationInterceptor 추상화
* [X] 완료 여부
* AuthenticationInterceptor의 후처리 로직을 추상화
```
public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
```
#### auth 패키지와 member 패키지에 대한 의존 제거
* [X] 완료 여부
* 현재 auth 패키지와 member 패키지는 서로 의존하고 있음
* UserDetailsService를 추상화 하여 auth -> member 의존을 제거하기
> [우아한 객체지향 세미나 - 패키지 의존 문제 해결](https://youtu.be/dJ5C4qRqAgA?t=2941)

#### SecurityContextInterceptor 추상화
* [ ] 완료 여부
```
public abstract class SecurityContextInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.clearContext();
    }
}
```