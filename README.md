# 🚀 즐겨찾기 기능 완성

---

## 1. 즐겨 찾기 기능 인수 테스트 작성
### -  즐겨찾기 생성 API
````
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
````

````
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
````

### - 즐겨찾기 조회 API
````
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
````
````
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
````
### - 즐겨찾기 삭제 API
````
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
````
````
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Mon, 22 Mar 2021 14:27:37 GMT
````
## 2. 즐겨 찾기 기능 개선하기
   모든 사람이 공유하는 즐겨찾기를 개인별로 관리할 수 있는 즐겨 찾기 기능 개선

## 3. 예외 처리
   - 즐겨 찾기 기능이 정상적으로 동작하기 위해 필요한 예외처리를 진행
   - 예시) 내가 등록하지 않은 즐겨 찾기를 제거 하려고 할 경우 등
   - 예외 처리에 대한 테스트는 어느 레이어에서 진행해야 할 지 고민

--- 
## TODO
- [x] 시나리오 작성
    - 즐겨찾기 생성
        - Scenario: 즐겨찾기 생성 성공
          ````
          Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재하는 노선을 생성한다.          
          When 종로3가역부터 시청역까지의 경로를 즐겨찾기로 생성한다.                                         
          Then 즐겨찾기 리스트 조회 시, 생성한 즐겨찾기가 조회된다.                                 
          ````
        - Scenario: 존재하지 않는 역이 포함된 경로를 즐겨찾기로 생성할 경우 생성 실패
          ````
          Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간들이 존재하는 노선을 생성한다.         
          When 종로3가역부터 서역까지의 경로를 즐겨찾기로 생성한다. 
          Then "역이 존재하지 않습니다."라는 메시지를 반환한다.                                  
          ````    
        - Scenario: 존재하지 않는 경로를 즐겨찾기로 생성할 경우 생성 실패
          ````
          Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간들이 존재하는 노선을 생성한다.         
          Given 상행역 : 동대문역 / 하행역 : 서울역 / 길이 : 20 인 구간들이 존재하는 노선을 생성한다.         
          When 종로3가역부터 동대문역까지의 경로를 즐겨찾기로 생성한다. 
          Then "경로가 존재하지 않습니다."라는 메시지를 반환한다.                                  
          ````     
    - 즐겨찾기 조회
        - Scenario: 즐겨찾기 조회 성공
          ````
          Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재하는 노선을 생성한다.          
          Given 종로3가역부터 시청역까지의 경로를 즐겨찾기로 생성한다.     
          When 즐겨찾기 리스트를 조회하면
          Then 생성한 즐겨찾기를 목록에서 조회할 수 있다.                          
          ````
    - 즐겨찾기 삭제      
        - Scenario: 즐겨찾기 삭제 성공
          ````
          Given 상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재하는 노선을 생성한다.          
          Given 종로3가역부터 시청역까지의 경로를 즐겨찾기로 생성한다.     
          When 생성한 즐겨찾기를 삭제하면
          Then 즐겨찾기 목록 조회 시 해당 즐겨찾기가 존재하지 앟는다.                       
          ````   
        - Scenario: 존재하지 않는 즐겨찾기를 삭제할 경우 삭제 실패
          ````
          When 존재하지 않는 즐겨찾기를 삭제하면  
          Then "즐겨찾기가 존재하지 않습니다."라는 메시지를 반환한다.  
          ````     
- [ ] 인수테스트 작성
- [ ] 단위테스트
- [ ] api 구현
