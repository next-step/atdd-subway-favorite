# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

즐겨찾기 기능 인수테스트

feature : 즐겨찾기 생성

    Given 강남역 생성
        And 양재역 생성
        And 교대역 생성
        And 신분당선 노선 생성 [강남역 - 양재역]
        And 2호선 노선 생성 [교대역 - 강남역]
        And 로그인이 되어 있음

    When 즐겨찾기 생성 요청 (교대역 ID/ 양재역 ID)

    Then 정상 응답

feature : 로그인이 안되었을 겨웅 즐겨찾기 생성 실패

    Given 신분당선 [강남역 - 양재역]
          2호선 [교대역 - 강남역]

    When 즐겨찾기 생성 요청 (논현역 ID/ 양재역 ID)

    Then 401 Unauthorized

feature : 즐겨찾기 조회

    Given 신분당선 [강남역 - 양재역]
          2호선 [교대역 - 강남역]

    When 즐겨찾기 생성 요청 (POST /favorites 교대역ID / 양재역ID)

    Then 401 Unauthorized

feature : 즐겨찾기 조회

    Given 신분당선 [강남역 - 양재역]
          2호선 [교대역 - 강남역]

    When 즐겨찾기 조회 요청 (GET /favorites)

    Then 401 Unauthorized

feature : 로그인이 안되었을때 즐겨찾기 조회 시 401 Unauthorized

    Given 신분당선 [강남역 - 양재역]
          2호선 [교대역 - 강남역]

    When 즐겨찾기 조회 요청 (GET /favorites)

    Then 401 Unauthorized


feature : 로그인이 되었을때 즐겨찾기 삭제

    Given 신분당선 [강남역 - 양재역]
          2호선 [교대역 - 강남역] 

    When 즐겨찾기 조회 요청 (GET /favorites)

    Then 401 Unauthorized

feature : 로그인이 안되었을때 즐겨찾기 삭제 시 401 Unauthorized

    Given 신분당선 [강남역 - 양재역]
          2호선 [교대역 - 강남역]

    When 즐겨찾기 조회 요청 (GET /favorites)

    Then 401 Unauthorized