package nextstep.member.config;

import java.util.List;

public enum AuthorizeRequest {
    WHITE_LIST(
            List.of(
                    "/login/token",
                    "/login/github",
                    "/stations/**",
                    "/members/**"
            )
    );

    private final List<String> list;

    AuthorizeRequest(List<String> list) {
        this.list = list;
    }

    public List<String> get() {
        return this.list;
    }
}
