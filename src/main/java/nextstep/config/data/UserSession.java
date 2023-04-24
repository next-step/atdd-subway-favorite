package nextstep.config.data;


import lombok.Getter;

@Getter
public class UserSession {
    private final Long id;

    public UserSession(Long id) {
        this.id = id;
    }
}
