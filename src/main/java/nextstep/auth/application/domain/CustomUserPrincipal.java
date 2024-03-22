package nextstep.auth.application.domain;

import lombok.Getter;

@Getter
public class CustomUserPrincipal {

    private final CustomUserDetail userDetail;

    public CustomUserPrincipal(CustomUserDetail userDetail) {
        this.userDetail = userDetail;
    }
}
