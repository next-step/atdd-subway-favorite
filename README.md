# 🚀 1단계 - 즐겨찾기 기능 완성

# 미션 소개

- 인증 기반의 인수 테스트를 연습해보는 미션입니다.
- FavoriteAcceptanceTest 파일에 인수 테스트를 작성하고 **즐겨 찾기 기능**을 완성하세요.
- JPA에 대한 내용은 미션을 수행하는데 필요한 만큼만 적용하세요. JPA에 대한 내용을 깊이있게 탐구하지 않으셔도 됩니다.
    - 즐겨찾기의 관계 매핑 시 cascade나 loading 등 설정에 대해서 깊이 있게 고민하지 않고 넘어가셔도 좋습니다.

# 요구사항

## 기능 요구사항

- `요구사항 설명`에서 제공되는 요구사항을 기반으로 **즐겨 찾기 기능**을 완성하세요.
- 예외 케이스에 대한 검증도 포함하세요.
    - 로그인이 필요한 API 요청 시 유효하지 않은 경우
    - 존재하지 않는 경로인 경우
    - 등등

## 프로그래밍 요구사항

- 인수 테스트 주도 개발 프로세스

  에 맞춰서 기능을 구현하세요.

    - `요구사항 설명`을 참고하여 인수 조건을 정의
    - 인수 조건을 검증하는 인수 테스트 작성
    - 인수 테스트를 충족하는 기능 구현

- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.

    - 뼈대 코드의 인수 테스트를 참고

- ```
  인수 테스트 이후 기능 구현은 TDD로 진행하세요.
  ```

    - 도메인 레이어 테스트는 필수
    - 서비스 레이어 테스트는 선택

# 요구사항 설명

## 1. 즐겨 찾기 기능 인수 테스트 작성

- 아래의 스펙에 맞춰 즐겨 찾기 기능의 인수 테스트를 작성하세요.
- 뼈대 코드로 제공되는 기능과는 다른 스펙이니 유의해주세요.

### API 종류

- 즐겨찾기 생성
- 즐겨찾기 조회
- 즐겨찾기 삭제

### Request / Response

#### 생성

```http
POST /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
content-type: application/json; charset=UTF-8
content-length: 27
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate

{
    "source": "1",
    "target": "3"
}
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
```

#### 조회

```http
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 22 Mar 2021 14:27:37 GMT
Keep-Alive: timeout=60
Connection: keep-alive

[
    {
        "id": 1,
        "source": {
            "id": 1,
            "name": "교대역"
        },
        "target": {
            "id": 3,
            "name": "양재역"
        }
    }
]
```

#### 삭제

```http
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Mon, 22 Mar 2021 14:27:37 GMT
```

## 2. 즐겨 찾기 기능 개선하기

- 현재 뼈대 코드로 제공되는 즐겨 찾기 기능은 모든 사람이 공유하는 즐겨찾기 입니다.
- 개인별로 관리할 수 있는 즐겨 찾기 기능으로 만들어주세요.

## 3. 예외 처리

- 즐겨 찾기 기능이 정상적으로 동작하기 위해 필요한 예외처리를 진행해주세요.
    - 예시) 내가 등록하지 않은 즐겨 찾기를 제거 하려고 할 경우 등
- 예외 처리에 대한 테스트는 어느 레이어에서 진행해야 할 지 고민해보세요.

# 힌트

## Favorite 클래스

- 필요한 경우 Favorite 클래스를 수정할 수 있습니다.
- 간접 참조와 직접 참조에 대해서 고민하신 후 Favorite <--> Station, Favorite <--> Member의 관계를 설정해주세요.

### 모두 직접 참조로 할 경우

- 지연 로딩이라던지 기타 관계 매핑에 필요한 설정은 자유롭게 해주셔도 좋습니다.

```java
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
    ...
```

### 모두 간접 참조로 할 경우

```java
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }
    ...
```

## 인증 정보 생성 코드 재사용

- 인증 기반의 인수 테스트 작성 시 인증 정보는 재사용 된다.
- 매번 새로 만들어주어도 상관 없지만 생성하는 로직을 재사용 하면 관리가 수월해진다.
- 예를 들어, 즐겨 찾기 생성 api 호출 시 토큰을 포함해야 한다면 토큰 생성을 공통 로직에 두어 재사용 할 수 있다.

## 비정상 경로를 즐겨찾기로 등록하는 경우

- 경로 조회 시 경로를 찾을 수 없거나 연결되지 않는 등 경로 조회가 불가능한 조회의 경우 즐겨찾기로 등록할 수 없도록 구현한다.

## 권한이 없는 경우 401 Unauthorized 응답

- 내 정보 관리 / 즐겨 찾기 기능은 로그인 된 상태에서만 가능
- 비로그인이거나 유효하지 않을 경우 401 Unauthorized 응답

## 테스트 레이어

- 예외 처리에 대한 테스트는 어느 레이어에서 진행해야 할 지 고민해보세요.
    - 모든 요구사항을 인수 테스트로 작성하는게 좋을지?
    - 아니면 세부 요구사항은 모두 단위 테스트로 작성하는게 좋을지?

# TODO

#### 인수테스트
##### 즐겨찾기 생성
로그인 전
-  [x] GIVEN 지하철 노선들을 생성하고 구간을 추가 후</br>
   WHEN 로그인을 하지 않고 출발역과 도착역을 입력하면</br>
   THEN 에러 처리와 함께 '로그인이 필요합니다.' 라는 메세지가 출력된다

로그인 후
-  [x] GIVEN 지하철 노선들을 생성하고 구간을 추가 후</br>
   THEN 출발역과 도착역을 입력하면 즐겨찾기가 등록된다</br>
   THEN 즐겨찾기 목록 조회 시 생성한 즐겨찾기를 찾을 수 있다
-  [x] GIVEN 지하철 노선들을 생성하고 구간을 추가 후</br>
   WHEN 출발역과 도착역을 같게 입력하면</br>
   THEN 에러 처리와 함께 '출발역과 도착역은 같을 수 없습니다.' 라는 메세지가 출력된다
-  [x] GIVEN 지하철 노선들을 생성하고 구간을 추가 후</br>
   WHEN 출발역과 도착역이 연결이 되지 않게 입력하면</br>
   THEN 에러 처리와 함께 '출발역과 도착역은 연결되어 있어야 합니다.' 라는 메세지가 출력된다
-  [x] GIVEN 지하철 노선들을 생성하고 구간을 추가 후</br>
   WHEN 존재 하지 않는 역을 입력하면</br>
   THEN 에러 처리와 함께 '입력한 역을 찾을 수 없습니다.' 라는 메세지가 출력된다

##### 즐겨찾기 조회
로그인 전
-  [x] GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후</br>
   WHEN 로그인을 하지 않고 즐겨 찾기 조회를 하면</br>
   THEN 에러 처리와 함께 '로그인이 필요합니다.' 라는 메세지가 출력된다

로그인 후
-  [x] GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후</br>
   WHEN 즐겨 찾기 조회를 하면</br>
   THEN 등록한 즐겨찾기 목록을 찾을 수 있다

##### 즐겨찾기 삭제
로그인 전
-  [ ] GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후</br>
   WHEN 로그인을 하지 않고 즐겨 찾기 삭제를 하면</br>
   THEN 에러 처리와 함께 '로그인이 필요합니다.' 라는 메세지가 출력된다

로그인 후
-  [ ] GIVEN 지하철 노선들을 생성하고 구간을 추가 그리고 즐겨찾기를 등록 후</br>
   WHEN 즐겨 찾기 삭제를 하면</br>
   THEN 삭제된 즐겨찾기 목록이 조회된다

#### 단위테스트

-  [x] case 1: 즐겨찾기를 등록 할 수 있다
-  [x] case 2: 즐겨찾기를 등록 시 출발역과 도착역이 같을 경우 에러 발생
-  [x] case 3: 즐겨찾기를 등록 시 출발역과 도착역을 포함하는 라인을 찾지 못했을 경우 에러 발생
-  [x] case 4: 즐겨찾기를 등록 시 시작역과 도착역을 찾을 수 없는 경우 에러 발생
