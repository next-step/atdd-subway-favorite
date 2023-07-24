package nextstep.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum GithubResponses {
  사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 13),
  사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 14),
  사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 15),
  사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 16);

  private String code;
  private String accessToken;
  private String email;
  private Integer age;

  GithubResponses(String code, String access_token, String email, int age) {
    this.code = code;
    this.accessToken = access_token;
    this.email = email;
    this.age = age;
  }

  public static GithubResponses findByCode(String code) {
    return Arrays.stream(GithubResponses.values())
        .filter(response -> response.getCode().equals(code))
        .findAny().orElseThrow(() -> new NoSuchElementException());
  }

  public static GithubResponses findByAccessToken(String accessToken) {
    return Arrays.stream(GithubResponses.values())
        .filter(response -> response.getAccessToken().equals(accessToken))
        .findAny().orElseThrow(() -> new NoSuchElementException());
  }

  public String getCode() {
    return this.code;
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  public String getEmail() {
    return this.email;
  }

  public Integer getAge() {
    return this.age;
  }

}
