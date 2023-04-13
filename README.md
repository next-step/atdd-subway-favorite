# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

---


# 🚀 1단계 - 로그인

## 요구사항
### 기능 요구사항
> - [x] 토큰생성
>   - [x] 아이디와 패스워드를 이용하여 토큰 생성하는 API
>   - [x] `AuthAcceptanceTest` 테스트 성공 시켜야함
>   - [x] 인수 테스트 실행 시 미리 데이터가 있어야 하는 경우 데이터를 초기화도 함께 수행하기
>     - `DataLoader`를 이용하여 초기 데이터를 설정한다.
> - [x] 토큰을 이용하여 내 정보 조회
>   - [x] 로그인하여 생성한 토큰을 이용하여 내 정보를 조회하는 API 
>   - [x] `MemberAcceptanceTest`의 `getMyInfo` 테스트 완성하기
>   - [x] `MemberController` 의 `findMemberOfMine` 메서드 구현하기

### 프로그래밍 요구사항
> - [x] 토큰을 이용한 단위 테스트 작성하기
>   - [x] 토큰생성 단위테스트

### 리뷰 요구사항
> - [x] 인증로직 Interceptor와 ArgumentResolver사용
> - [x] 타입추론 사용
>   - 개발자가 변수의 타입을 명시적으로 적어주지 않고도, 타입을 컴파일러에서 알아서 이 변수의 타입을 대입된 리터럴로 추론하는 것.
> - [x] class 명 알맞게 변경 @AuthMember -> @PreAuthorize 


---


# 🚀 2단계 - 깃헙 로그인

## 요구사항
### 기능 요구사항
- [x] 깃허브를 이용한 로그인 구현(토큰 발행)
>  - [x] `AuthAcceptanceTest` 테스트 만들기
- [x] 가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행

### 프로그래밍 요구사항
>- [x] GitHub 로그인을 검증할 수 있는 인수 테스트 구현(실제 GitHub에 요청을 하지 않아도 됨)

### 리뷰 요구사항
>- [ ] 인수테스트 추가하기
>- [ ] api호출 로직과 db호출로직 분리
>- [ ] 에러 메세지 변경 
