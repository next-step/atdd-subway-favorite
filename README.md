# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 1단계 - 즐겨찾기 기능 완성

### 1. 즐겨 찾기 기능 인수 테스트 작성

- 아래의 스펙에 맞춰 즐겨 찾기 기능의 인수 테스트를 작성하세요.
- [ ] 즐겨찾기 생성
- [ ] 즐겨찾기 조회
- [ ] 즐겨찾기 삭제

### 2. 즐겨 찾기 기능 개선하기

- 현재 뼈대 코드로 제공되는 즐겨 찾기 기능은 모든 사람이 공유하는 즐겨찾기 입니다.
- [ ] 개인별로 관리할 수 있는 즐겨 찾기 기능으로 만들어주세요.

### 3. 예외 처리

- 즐겨 찾기 기능이 정상적으로 동작하기 위해 필요한 예외처리를 진행해주세요.
- [ ] 내가 등록하지 않은 즐겨 찾기를 제거 하려고 할 경우
- 예외 처리에 대한 테스트는 어느 레이어에서 진행해야 할 지 고민해보세요.

### 인증 정보 생성 코드 재사용

- [x] 인증 기반의 인수 테스트 작성 시 인증 정보는 재사용 된다.
- [x] 매번 새로 만들어주어도 상관 없지만 생성하는 로직을 재사용 하면 관리가 수월해진다.
- [x] 예를 들어, 즐겨 찾기 생성 api 호출 시 토큰을 포함해야 한다면 토큰 생성을 공통 로직에 두어 재사용 할 수 있다.

### 비정상 경로를 즐겨찾기로 등록하는 경우

- [ ] 경로 조회 시 경로를 찾을 수 없거나 연결되지 않는 등 경로 조회가 불가능한 조회의 경우 즐겨찾기로 등록할 수 없도록 구현한다.

### 권한이 없는 경우 401 Unauthorized 응답

- [ ] 내 정보 관리 / 즐겨 찾기 기능은 로그인 된 상태에서만 가능
- [ ] 비로그인이거나 유효하지 않을 경우 401 Unauthorized 응답

### 테스트 레이어

- 예외 처리에 대한 테스트는 어느 레이어에서 진행해야 할 지 고민해보세요.
- 모든 요구사항을 인수 테스트로 작성하는게 좋을지?
- 아니면 세부 요구사항은 모두 단위 테스트로 작성하는게 좋을지?
