package nextstep.auth.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.domain.Member;

public interface OAuth2User {
    Long getId();
    String getUsername();
}
