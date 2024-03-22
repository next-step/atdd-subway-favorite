package nextstep.auth.application.domain;

import lombok.Getter;

@Getter
public class CustomUserPrincipal {

    private final String id;

    public CustomUserPrincipal(String id) {
        this.id = id;
    }


}
