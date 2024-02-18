# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 인수 테스트와 인증
## 1단계 - 즐겨찾기 기능 완성
### 요구사항
#### 1. 즐겨찾기 기능 인수 테스트 작성
- 즐겨찾기 생성
  - Request
  ```shell
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
  ```
  - Response
  ```shell
  HTTP/1.1 201 Created
  Keep-Alive: timeout=60
  Connection: keep-alive
  Content-Length: 0
  Date: Mon, 22 Mar 2021 14:27:37 GMT
  Location: /favorites/1
  ```
    - 성공 시나리오
      > Given 로그인을 한 뒤<br>
       Given 지하철 노선을 생성하고<br>
       When 출발역과 도착역을 통해 경로를 즐겨찾기에 추가 하면<br>
       Then 즐겨찾기 목록 조회 시 생성한 즐겨찾기를 찾을 수 있다
    - 실패 시나리오
      > Given 지하철 노선을 생성하고<br>
      When 출발역과 도착역을 통해 경로를 즐겨찾기에 추가 하는데<br>
      When 로그인이 되어 있지 않다면<br>
      Then 에러가 난다.

      > Given 로그인을 한 뒤<br>
      Given 지하철 노선을 생성하고<br>
      When 출발역과 도착역을 통해 경로를 즐겨찾기에 추가 하는데<br>
      When 존재하지 않는 경로인 경우<br>
      Then 에러가 난다.
  
      > Given 로그인을 한 뒤<br>
      Given 지하철 노선을 생성하고<br>
      When 출발역과 도착역을 통해 경로를 즐겨찾기에 추가 하는데<br>
      When 이미 추가된 즐겨찾기 경로라면 <br>
      Then 에러가 난다.

- 즐겨찾기 조회
  - Request
  ```shell
  GET /favorites HTTP/1.1
  authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
  accept: application/json
  host: localhost:60443
  connection: Keep-Alive
  user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
  accept-encoding: gzip,deflate
  ```
   - Response
  ```shell
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
  - 성공 시나리오
      > Given 로그인을 한 뒤<br>
       Given 지하철 노선을 생성하고<br>
       Given 경로들을 즐겨찾기에 추가 한 뒤<br>
       When 즐겨찾기 목록을 조회 하면<br>
       Then 등록된 즐겨찾기 목록을 조회할 수 있다.
  - 실패 시나리오
      > Given 지하철 노선을 생성하고<br>
       When 즐겨찾기 목록을 조회 하는데<br>
       When 로그인이 되어 있지 않다면
       Then 에러가 난다.

- 즐겨찾기 삭제
  - Request
  ```shell
  DELETE /favorites/1 HTTP/1.1
  authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
  accept: */*
  host: localhost:60443
  connection: Keep-Alive
  user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
  accept-encoding: gzip,deflate
  ```
  - Response
  ```shell
  HTTP/1.1 204 No Content
  Keep-Alive: timeout=60
  Connection: keep-alive
  Date: Mon, 22 Mar 2021 14:27:37 GMT
  ```
    - 성공 시나리오
      > Given 로그인을 한 뒤<br>
      Given 지하철 노선을 생성하고<br>
      Given 경로들을 즐겨찾기에 추가 한 뒤<br>
      When 즐겨찾기를 삭제하면<br>
      Then 즐겨찾기 목록조회시 제외되어있다.
    - 실패 시나리오
      > Given 지하철 노선을 생성하고<br>
      When 즐겨찾기를 삭제하는데<br>
      When 로그인이 되어 있지 않다면
      Then 에러가 난다.

      > Given 로그인을 한 뒤<br>
      Given 지하철 노선을 생성하고<br>
      When 없는 즐겨찾기를 삭제하면<br>
      Then 에러가 난다.

#### 2. 즐겨찾기 기능 개선하기
- [x] 개인별로 즐겨찾기 기능으로 개선
#### 3. 예외 처리
- [x] 예외처리

## 2단계 - 깃헙 로그인 구현
### 요구사항
#### 깃허브를 이용한 로그인 구현(토큰 발행) - 실제 GitHub 에 요청 X
- 깃헙 로그인 API
    ```shell
    # Request
    POST /login/github HTTP/1.1
    content-type: application/json
    host: localhost:8080
    
    {
      "code": "qwerasdfzxvcqwerasdfzxcv"
    }
    
    # Response
    {
      "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
    }
    ```
    - [x] 구현한 server 로 요청을 보낸 뒤 server 에서 github 으로 요청을 보내는 방식으로 구현
    - [x] 가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행
    - [x] accessToken 는 깃헙으로부터 받아온게 아니라 subway 애플리케이션에서 아이디/패스워드를 이용한 로그인 시 응답받는 토큰과 동일한 토큰
    - [x] 어떤 코드로 요청이 오면 정해진 response 를 응답하는 구조 구성
    - 성공 시나리오
      > When Github 로그인 요청시<br>
        Then 토큰을 발급받을 수 있다.
    
    - 실패 시나리오
      > When Github 로그인 요청시<br>
        When Github 로그인 요청에 실패하면<br>
        Then 에러가 난다.

## 3단계 - 패키지 리팩터링
### 요구사항
- [x] 인증관련 부분을 다른 프로젝트에서 재사용할 수 있는 수준으로 리팩터링
  - email/password 로그인 로직과 github 로그인 로직에서 추상화 할 수 있는 부분은 추상화
  - 중복 제거를 할 수 있는 부분은 제거
  - 리팩터링 간 패키지 사이의 상호 참조가 발생할 수 있는데 이 부분을 최대한 방지
