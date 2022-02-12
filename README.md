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

## 💻 로그인 인증 프로세스 실습
### 요구사항
- [] 패키지 구조 리팩터링(선택)
  - 뼈대 코드의 구조를 자신이 편한 구조로 리팩터링 하세요.
  - subway는 2주차까지의 미션의 샘플 코드 입니다.
  - auth는 인증과 관련된 로직입니다
  - member는 회원 관리 관련된 로직 입니다.

- [] MemberAcceptanceTest의 인수 테스트 통합하기
  - 인수 조건
    ~~~
    Feature: 회원 정보를 관리한다.
    
      Scenario: 회원 정보를 관리
      When 회원 생성을 요청
      Then 회원 생성됨
      When 회원 정보 조회 요청
      Then 회원 정보 조회됨
      When 회원 정보 수정 요청
      Then 회원 정보 수정됨
      When 회원 삭제 요청
      Then 회원 삭제됨
    ~~~

- [] AuthAcceptanceTest의 myInfoWithSession 테스트 메서드를 성공 시키기
  - GET /members/me 요청을 처리하는 컨트롤러 메서드를 완성하여 myInfoWithSession 인수 테스트를 성공시키세요
    - MemberController의 findMemberOfMine메서드를 구현하면 위의 요청을 처리할 수 있습니다.
  - Controller에서 로그인 정보 받아오기
    - SecurityContextHolder에 저장된 SecurityContext를 통해 Authentication 객체 조회
    - Authentication에 저장된 LoginMember의 정보로 존재하는 멤버인지 확인 후 멤버 정보 조회
  
