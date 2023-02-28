package nextstep.subway.fake;

import nextstep.member.domain.Member;

public enum GithubResponseFixture {

    Github사용자1("a123a123", "access.access1", "member@naver.com"),
    Github사용자2("b123b123", "acess.access2", "member2@kakao.com");

    private String code;
    private String accessToken;
    private String email;


    GithubResponseFixture(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
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

    public Member toMember() {
        return new Member(this.getEmail());
    }
}
