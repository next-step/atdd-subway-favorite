package nextstep.subway.auth.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.subway.auth.domain.UserDetails;

public interface LoginMemberPort {
    UserDetails getLoginMember(String principal);

    UserDetails getUserDetailFromPayload(String payload) throws JsonProcessingException;
}
