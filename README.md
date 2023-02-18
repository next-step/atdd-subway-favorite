# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 1단계 - 로그인
1. 기능 요구사항
   1. 토큰 생성
      1. 아이디와 패스워드를 이용하여 토큰을 생성하는 API
      2. AuthAcceptanceTest 테스트 성공 시켜야함
      3. 인수 테스트 실행 시 미리 데이터가 있어야 하는 경우 데이터를 초기화도 함께 수행하기
   2. 토큰을 이용하여 내 정보 조회
      1. 로그인하여 생성한 토큰을 이용하여 내 정보를 조회하는 API
      2. MemberAcceptanceTest의 getMyInfo 테스트 완성하기
      3. MemberController 의 findMemberOfMine 메서드 구현하기
2. 프로그래밍 요구사항
   1. 토큰을 이용한 인수 테스트를 작성하기