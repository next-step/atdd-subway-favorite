# 🚀 2단계 - 깃헙 로그인 구현

# 요구사항

## 기능 요구사항

- [ ] 깃허브를 이용한 로그인 구현(토큰 발행)
- [ ] 가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행

## 프로그래밍 요구사항

- [ ] GitHub 로그인을 검증할 수 있는 인수 테스트 구현(실제 GitHub에 요청을 하지 않아도 됨)

# 요구사항 설명

## 깃헙 로그인 API

- `AuthAcceptanceTest` 테스트 만들기

> `/login/github` 요청으로 `accessToken`응답을 받는 API 입니다. client에서 직접 github으로 요청을 보내는게 아니라 우리가 구현한 server로 요청을 보낸 뒤 server에서 github으로 요청을 보내는 방식으로 구현하세요.

### Request

```http
POST /login/github HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "code": "qwerasdfzxvcqwerasdfzxcv"
}
```

### Response

- accessToken는 깃헙으로부터 받아온게 아니라 subway 애플리케이션에서 생성한 토큰
- 아이디/패스워드를 이용한 로그인 시 응답받는 토큰과 동일한 토큰

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
}
```

## code 별 응답 response

- 매번 실제 깃헙 서비스에 요청을 보낼 수 없으니 어떤 코드로 요청이 오면 정해진 response를 응답하는 구조를 만든다.

# 힌트

## 1단계 - 실패하는 테스트 실행하기

- `AuthAcceptanceTest`의 `githubAuth` 테스트를 실행하세요.
  ![image.png](https://nextstep-storage.s3.ap-northeast-2.amazonaws.com/0e6ebb9d56f74e788c8645eda9364e42)

```java
@DisplayName("Github Auth")
@Test
void githubAuth() {
    Map<String, String> params = new HashMap<>();
    params.put("code", "code");

    ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/github")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();

    assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
}
```

- 실패하는 테스트의 에러로그를 확인합니다.

```plaintext
java.lang.NullPointerException: null
	at nextstep.auth.token.oauth2.github.GithubClient.getAccessTokenFromGithub(GithubClient.java:42) ~[classes/:na]
	at nextstep.auth.token.TokenService.createTokenFromGithub(TokenService.java:43) ~[classes/:na]
```

## 2단계 - 깃헙 대신 다른 곳으로 요청 보내기

- 깃헙으로의 요청과 응답을 관리하는 객체(GithubClient)를 만들기 위한 TDD를 진행하세요.

```java
public class GithubClientTest {
    ...
}
```

- github 토큰 조회를 위한 요청 코드 예시

```java
    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                "clientId", // client id
                "clientSecret" // client secret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String url = "url"; // github token request url
        String accessToken = restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();

        return accessToken;
    }
```

- GithubClient을 구현할 때는 깃헙이 아닌 다른 곳으로 요청을 보내어 응답할 수 있도록 설정하세요.

```java
public class TestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = "access_token";
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubProfileResponse response = new GithubProfileResponse("email@email.com", 20);
        return ResponseEntity.ok(response);
    }
}
```

## 이후

- 나머지 코드를 작성하세요.
- 실제 구현 시 인증 서버는 매번 새로운 code를 응답하고 code에 따라 사용자를 인증합니다.
- 테스트 환경에서는 사용자별로 고정 code를 사용할 수 있도록 하세요.

```java
public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    ...
```
