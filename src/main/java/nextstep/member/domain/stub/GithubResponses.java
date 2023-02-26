package nextstep.member.domain.stub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.application.message.Message;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum GithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    public static GithubResponses findByCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(Message.INVALID_CODE));
    }
}
