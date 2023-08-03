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