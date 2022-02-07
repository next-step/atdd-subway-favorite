
```properties
Feature: 회원 정보를 관리한다.

  Scenario: 회원 정보를 관리
    When 회원 생성을 요청
    Then 회원 생성됨
    When 회원 정보 조회 요청
    Then 회원 정보 조회됨
    When 회원 정보 수정 요청
    Then 회원 정보 수정됨
    When 회원 삭제 요청
    Then 회원 삭제됨

  Scenario: 나의 정보를 관리
    When 회원 생성을 요청
    Then 회원 생성됨
    When 로그인 요청 
    Then 로그인 됨 
    When 회원 정보 조회 요청
    Then 회원 정보 조회됨
    When 회원 정보 수정 요청
    Then 회원 정보 수정됨
    When 회원 삭제 요청
    Then 회원 삭제됨
```