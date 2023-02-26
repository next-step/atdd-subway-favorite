package nextstep.member.auth;

public class BaseOAuth2User implements OAuth2User {

    private String name;

    public BaseOAuth2User() {
    }

    public BaseOAuth2User(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
