package nextstep.github;

import java.util.Arrays;

public enum FakeGithubProfileResponse {

    사용자1("email1@email.com", 20),
    사용자2(  "email2@email.com", 21),
    사용자3(  "email3@email.com", 22),
    사용자4(  "email4@email.com", 23),
    EMPTY("EMPTY", 19);

    private String email;
    private int age;

    FakeGithubProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() { return email; }
    public int getAge() { return age; }

    public static FakeGithubProfileResponse findByEmail(String email) {
        return Arrays.asList(FakeGithubProfileResponse.values()).stream()
                .filter(response -> response.equalsByEmail(email))
                .findFirst()
                .orElse(EMPTY);
    }

    private boolean equalsByEmail(String email) {
        if (this.email.equals(email)) { return true; }
        return false;
    }
}