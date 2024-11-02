package nextstep.member.application.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GithubProfileResponse {

    private String email;
    private Integer age;

    @Builder
    private GithubProfileResponse(String email, Integer age) {
        this.email = email;
        this.age = age;
    }

    public static GithubProfileResponse of(String email, Integer age) {
        return GithubProfileResponse.builder()
                .email(email)
                .age(age)
                .build();
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
