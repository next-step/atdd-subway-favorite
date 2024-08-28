package nextstep.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.common.exception.MemberNotFoundException;
import nextstep.common.response.ErrorCode;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 10),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 20),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 30),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com",40);

    private String code;
    private String accessToken;
    private String email;
    private int age;


    public static GithubResponses getUserByCode(String code) {
        return Stream.of(values())
                .filter(user -> code.equals(user.code))
                .findFirst()
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public static GithubResponses getUserByToken(String accessToken) {
        return Stream.of(values())
                .filter(user -> accessToken.equals(user.accessToken))
                .findFirst()
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
