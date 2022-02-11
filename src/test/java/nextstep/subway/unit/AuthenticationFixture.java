package nextstep.subway.unit;

import nextstep.member.domain.LoginMember;

public class AuthenticationFixture {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    public static final LoginMember FIXTURE_LOGIN_MEMBER = new LoginMember(1L, EMAIL, PASSWORD, 20);
}
