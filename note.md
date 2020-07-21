# 로그인 리팩터링 미션

1. 1,2단계에서 구현한 인증 로직에 대한 리팩터링을 진행하세요  
    - SessionAuthenticationInterceptor
    - TokenAuthenticationInterceptor
    - SessionSecurityContextPersistenceInterceptor
    - TokenSecurityContextPersistenceInterceptor
    
    크게 authentication과 persistence가 있는데 고민 좀 해보자.  
    2단계에서 리팩토링하려고 했던건데 했으면 할거 없어져서 큰일날뻔..'ㅁ'
2. 내 정보 수정 / 삭제 기능을 처리하는 기능을 구현하세요.
    - ez ez
3. HandlerMethodArgumentResolver를 활용하여 Controller에서 Authentication에 접근해보세요.
    - ez ez
4. auth 패키지와 member 패키지에 대한 의존 제거
    - 현재 auth 패키지와 member 패키지는 서로 의존하고 있음
    - UserDetailsService를 추상화 하여 auth -> member 의존을 제거하기
    
4번.. 이 문제인데 클린 아키텍처 공부할때 삽질해봤던 DIP를 여기에 적용하면 간단히 끝날 듯?  
현재 auth와 member간 발생한 문제점은 다음과 같다고 본다.    
- auth layer 전반적(인증 인터셉터)으로 CustomUserDetailsService를 사용하고 있음

따라서 내가 선택한 것은
- UserDetailService라는 인터페이스만 auth 레이어에 있고
- CostomUserDetailService는 UserDetailService의 구현체로 바꾸고
- 어차피 스프링에 등록된 빈이니까 인터셉터에 저 구현체를 넣어주면 해결
- 아 그 친구의 return 타입 LoginMember도 추상화를 해야겠지.

일단 행복회로 돌려봄.