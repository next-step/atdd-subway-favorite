package subway.auth.token;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class TokenRequest {

    public static final String EMAIL_NOT_BLANK = "이메일은 필수입니다";
    public static final String EMAIL_NOT_VALID = "이메일 형식이 아닙니다.";
    public static final String PASSWORD_NOT_BLANK = "패스워드는 필수입니다";

    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = EMAIL_NOT_VALID)
    private String email;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;
}
