package subway.auth.token.oauth2.github;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class GithubTokenRequest {
    public static final String CODE_NOT_BLANK = "코드 값은 필수 입니다.";
    @NotBlank(message = CODE_NOT_BLANK)
    private String code;
}
