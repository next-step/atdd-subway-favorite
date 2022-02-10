<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```

### step0 생각해보기
- 인수테스트 통합을 했으면 기존 인수테스트는 지워야 하는게 맞지 않는가?

### step1 피드백 정리
- 인수 테스트 통합은 기존에 분리되어 있던 인수 테스트를 리팩터링 한 것으로 볼 수 있기 때문에 지워주는 것이 맞다. - 통합된 과정 속에서 중요하게 검증해야 할 것이 있다면 남겨 놓는 것이 좋음.
- ObjectMapper 객체를 스프링 빈으로 관리하여 주입시켜 보자. - 이미 빈으로 등록되어 사용되고 있었다. 새로 빈으로 등록해주자. 스프링 자체 로직 중 objectMapper를 사용하던 곳에서 에러가 났다.
- TokenAuthenticationInterceptorTest 실제 객체로도 단위 테스트 구현해보기. - jwt토큰을 생성하고 검증하는 부분에서 실제 객체를 사용하면 iat, exp가 달라지므로, jwt토큰 값을 검증할 수가 없었다. 그래서 1. jwt토큰 생성하는 부분만 Mock으로 진행하던지, 2. 아니면 값을 검증하지는 말고, notNull 체크만 하던지, 3. validate를 검증하는 방법 중 한 가지를 고르면 되지 않을까?
- memberService.findMember()에 객체를 넘겨주는 것과, 객체의 특정 값을 넘겨주는 것에 대해 차이를 생각해보기(객체 의존 기준으로 생각해보기) - 객체를 넘겨주면 의존이 생기게 되는데, 의존이 생긴다는 것은 객체를 수정하게 되면 의존하는 객체도 같이 변경이 이루어져야한다는 걸 의미한다. 의존이 나쁜 것은 아니다. 각 객체가 자신의 역할을 맡고, 서로 의존관계를 맺으며 협력하는 것이 객체지향이니깐.
  여기서 봐야할 것은 넘기는 대상이 단순한 Dto인가, 역할을 맡은 객체인가 하는 것이다. 단순 Dto라면 memberService에서 많이 사용되는지 정도만 생각하면 될 것 같다. 도메인이라면, 의존관계의 흐름 방향(도메인을 서비스가 사용) 정도를 보고, 서비스 안에서 맡은 역할이 있는지를 봐야할 것 같다.

### step2 질문
- `AuthenticationConverter.prehandle()`에서 구현체 Session관련쪽은 IOException이 없는데도 남겨주는 것에는 문제가 없는건가 궁금합니다.
- 추상화 클래스 만들때 Abstract- 로 안 만들어도 되는지?
- private final을 쓰지 않는 이유에 대해서 궁금하다.
- LoginMember도 패키지 의존성이 존재하는 것 같아서 UserDetails라는 클래스 이름으로 변경해보았는데 어떤지랑, TokenAuthenticationInterceptorMockTest.prehandle()테스트에서 LoginMember 의존관계가 남아있는데, 없앨 수 있는 방법이 있는지 궁금.
- 패키지명 변경이나 클래스 이름 변경할 경우. 커밋을 따로 하는지 궁금하고, 따로 한다면 commit convention에 따라 prefix를 뭐로 쓰시는지 궁금합니다. 더하자면.. chore의 의미가 무엇인지 잘 이해가 가지 않아요.. 하기 싫은 일이라는 의미도 잘 모르겠습니다.

### step2 피드백 정리
- objectMapper는 생성비용이 비싸다. 그렇기 때문에 빈으로 주입받는게 좋다.
- 중복되는 테스트 코드를 추상화하면 테스트 클래스에서 직접 의존하는 객체의 수가 줄어든다.
- 실제 객체를 이용하여 테스트 코드를 작성하면, 내부 로직에 의존하지 않는 테스트가 완성된다.
- 메서드 내에 가독성을 위한 메서드 분리를 활용하자.
- 인터페이스의 메서드를 결정할 때, 구현될 객체를 알 수 없으므로, 미리 한 방향으로 정하도록 하자. - 예외처리를 내부에서 할 수도 있고(throw제거), 호출하는 쪽에서 하게 할 수도 있다.(throw추가)
- 메서드의 인자로 객체를 넘길지, 값을 넘길지는, 객체에게 위임할 행위와, 의존관계를 고려하자.
- 리팩터링:
  1. 기존 로직을 검증하는 테스트 코드가 없다면 작성.
  2. 리팩터링을 위한 실패하는 테스트 작성.
  3. 기능 구현.
  4. 리팩터링 적용(기존코드랑 공존)
  5. 기존 프로덕션 코드 모두 미사용이면 제거
  6. 기존 테스트 코드 미사용시 제거
- 인터페이스 구현체를 테스트용으로 만들어놓는 것도 좋은 방법인 것 같다.

### step3 질문
