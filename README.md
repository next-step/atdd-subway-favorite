# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션


# merge 이후 미션 수행 명령어
```
git checkout songteaheon  
git fetch upstream songteaheon  
git rebase upstream/songteaheon
```  


# 할 일
## 1단계 - 내 정보 조회 기능 구현
- [X] 인수 테스트 목록 작성
- [X] 인수 테스트 작성
- [X] 코드 구현

## 2단계 - 깃헙 로그인 인수 테스트
- [X] AuthAcceptanceTest의 githubAuth 테스트를 성공시키기 (step1)
- [X] GithubClient의 요청이 Github이 아닌 GithubTestController에서 처리하게 하기(step1)
- [X] 코드 구현 (code에 해당하는 사용자 정보를 이용하여 AccessToken을 응답받기)

## 3단계 - 즐겨찾기 기능 구현
- [X] 인수 테스트 목록 작성
- [X] 생성 인수 테스트 작성
- [X] 조회 인수 테스트 작성
- [X] 삭제 인수 테스트 작성
- [X] 로그인 안된 사용자 인수 테스트 작성
- [X] 코드 구현
- [X] 생성 인수 코드 작성
- [X] 조회 인수 코드 작성
- [X] 삭제 인수 코드 작성
- [X] 1차 리뷰 반영
  - [X] Favorite Service 테스트 
  - [X] Member에 favorite 개념 종속 시키기
  - [X] Favorite Eager 삭제 -> 질문
  - [X] Favorite 불필요 팩토리 메서드 제거
  - [X] save 후 location 내려주기
  - [X] 불필요한 개행 삭제
- [X] 2차 리뷰 반영
  - [ ] 즐겨찾기의 지하철역 정보는 1:1이 아니라 N:1 관계로 수정
  - [ ] CQRS패턴 적용
  - [ ] favorites 일급 컬렉션 적용
  - [ ] getFavorites() 불변성 적용
  - [ ] 즐겨찾기에 대한 핸들링을 member가 아닌 일급컬렉션에서 하도록 수정


## 패키지 의존성
- favorite -> member, subway, auth
- member -> auth