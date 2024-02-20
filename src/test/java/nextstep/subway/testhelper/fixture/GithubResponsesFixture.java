package nextstep.subway.testhelper.fixture;

import java.util.Arrays;
import java.util.Objects;

public enum GithubResponsesFixture {
    사용자1("aofijeowifjaoief", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiaWF0IjoxNzA4Mzg5MDk3LCJleHAiOjE3MDgzOTI2OTd9.g82LQ5UflW9pxKpofFz1AFDHehLEw1hM5EaNIGuCDTY", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 10),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 15),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 30),
    UNKNOWN("", "", "", 0);

    private final String code;
    private final String accessToken;
    private final String email;
    private final Integer age;

    GithubResponsesFixture(String code,
                           String accessToken,
                           String email,
                           Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubResponsesFixture findByCode(String code) {
        return Arrays.stream(GithubResponsesFixture.values())
                .filter(response -> Objects.equals(response.code, code))
                .findFirst()
                .orElse(GithubResponsesFixture.UNKNOWN);
    }

    public static GithubResponsesFixture findByToken(String accessToken) {
        return Arrays.stream(GithubResponsesFixture.values())
                .filter(response -> Objects.equals(response.accessToken, accessToken))
                .findFirst()
                .orElse(GithubResponsesFixture.UNKNOWN);
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
