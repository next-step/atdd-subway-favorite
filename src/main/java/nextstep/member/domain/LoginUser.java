package nextstep.member.domain;

public class LoginUser {

    private final Long userId;

    public LoginUser(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

}
