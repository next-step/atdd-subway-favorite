
# 📚 즐겨찾기 기능 인수 조건
```
Feature: 즐겨찾기 생성 기능

    Senario: 회원이 즐겨찾기를 생성한다.
        Given: 로그인 토큰을 발급받는다.
        When: 즐겨찾기를 생성한다.
        Then: 성공(201 Created) 응답을 받는다.
        And: Location URL로 즐겨찾기를 조회한다.
        And: 즐겨찾기 목록을 검증한다.
        
        
    Senario: 비회원이 즐겨찾기를 생성한다.
        When: 즐겨찾기를 생성한다.
        Then: 실패(401 Unauthorized) 응답을 받는다.
    
    
    Senario: 회원이 즐겨찾기를 조회한다.
        Given: 로그인 토큰을 발급받는다.
        And: 즐겨찾기를 생성한다.
        When: 즐겨찾기를 조회한다.
        Then: 성공(200 OK) 응답을 받는다.
        And: 즐겨찾기 목록을 검증한다.
    
        
    Senario: 비회원이 즐겨찾기를 조회한다.
        When: 즐겨찾기를 조회한다.
        Then: 실패(401 Unauthorized) 응답을 받는다.

        
    Senario: 회원이 즐겨찾기를 삭제한다.
        Given: 로그인 토큰을 발급받는다.
        And: 즐겨찾기를 생성한다.
        And: Location URL로 즐겨찾기를 삭제한다.
        Then: 성공(204 No Content) 응답을 받는다.
        
        
    Senario: 비회원이 즐겨찾기를 삭제한다.
        When: 즐겨찾기를 삭제한다.
        Then: 실패(401 Unauthorized) 응답을 받는다.
```