# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

### 요구사항
#### step0
- AuthAcceptanceTest의 bearerAuth 테스트를 실행시켜 보세요
- 토큰 발급이 어떤 방식으로 이루어지는지 확인하세요.

#### step1
- 내 정보 조회 기능에 대한 인수 조건을 도출 후 인수 테스트 작성 및 기능 구현

#### step2
 - AuthAcceptanceTest의 githubAuth 테스트를 성공시키기
   - GithubClient의 요청이 Github이 아닌 GithubTestController에서 처리하게 하기
   - code에 해당하는 사용자 정보를 이용하여 AccessToken을 응답받기

#### step2
 - 즐겨찾기 기능 구현
   - 즐겨찾기 생성
   - 즐겨찾기 조회
   - 즐겨찾기 삭제