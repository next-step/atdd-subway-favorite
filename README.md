# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 1단계 - 내 정보 조회 기능 구현

### 요구사항
- 내 정보 조회 기능을 구현하기
  - url path가 아닌 header(authorization)을 이용하여 사용자 식별

## 🚀 2단계 - 깃헙 로그인 인수 테스트

### 요구사항
- AuthAcceptanceTest의 githubAuth 테스트 성공시키기
  - GithubClient의 요청이 Github이 아닌 GithubTestController에서 처리하게 하기
  - code에 해당하는 사용자 정보를 이용하여 AccessToken을 응답받기

## 🚀 3단계 - 즐겨찾기 기능 구현

### 요구사항
- 즐겨찾기 생성 기능
  - 경로 조회가 불가능한 경우 즐겨찾기 생성 불가
- 즐겨찾기 조회 기능
- 즐겨찾기 삭제 기능
- 모든 요청은 권한 인증 필요
  - 권한이 없는 경우 401 Unauthorized 응답

### 인수조건
```
Given 지하철 노선과 역을 생성하고
And 회원가입을 생성하고
When 출발역과 도착역으로 즐겨찾기 생성 요청을 하면
Then 즐겨찾기 조회 시 생성된 즐겨찾기를 확인할 수 있다

Given 지하철 노선과 역을 생성하고
And 회원가입을 생성하고
When 경로조회가 불가능한 출발역과 도착역으로 즐겨찾기 생성 요청을 하면
Then 즐겨찾기 생성에 실패한다

Given 지하철 노선과 역을 생성하고
And 회원가입을 생성하고
And 즐겨찾기를 생성하고
When 생성된 즐겨찾기 삭제 요청을 하면
Then 해당 즐겨찾기는 삭제된다

Given 지하철 노선과 역을 생성하고
When 권한 없이 즐겨찾기 생성 요청을 하면
Then 권한 오류가 발생한다
```
