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
      When 이미 추가된 즐겨찾기 라면 <br>
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

#### 2. 즐겨찾기 기능 개선하기
#### 3. 예외 처리
