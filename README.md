# 인수 테스트와 인증

## 인증 기반 인수 테스트

## 기능 요구사항

- Form 기반 로그인과 Bearer 기반 로그인 기능 구현
    - `UsernamePasswordAuthenticationFilter`  `BearerTokenAuthenticationFilter` `AuthAcceptanceTest`
- 지하철역, 노선, 구간을 변경하는 API는 관리자만 가능하도록 수정
    - Token을 이용한 로그인을 통해 관리자 여부를 판단
    - Token 값은 Header에서 추출
    - 생성, 수정, 삭제 API
    - 그외는 모든 사용자가 접근 가능하도록 (조회 API)

## 요구사항 설명

### 관리자 전용 API 접근 제한

- 관리자 전용 API에 대해 접근 기능을 구현하려면 관리자 멤버와 역할이 미리 설정되어있어야 함
- 인수 테스트를 수행하기 전 공통으로 필요한 멤버, 역할은 초기에 설정할 수 있도록
- DataLoader를 활용 해보자

### 권한 검증

- API 별 권한 검증을 위해 @Secured 활용
- 관리자만 접근 가능한 기능을 검증하는 인수 테스트 만들기
- 관리자 여부는 Header에 담기는 Token 정보를 이용
- 토큰이 유효하지 않은 경우 에러 응답이 오는지 확인'