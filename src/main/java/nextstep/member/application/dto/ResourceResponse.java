package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceResponse {

    private String email;

    public ResourceResponse(String email) {
        this.email = email;
    }
}
