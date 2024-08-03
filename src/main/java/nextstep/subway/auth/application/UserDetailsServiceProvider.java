package nextstep.subway.auth.application;

import nextstep.subway.auth.application.dto.UserDetailsRequest;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsServiceProvider {
    private List<UserDetailsService> userDetailsServices;

    public UserDetailsServiceProvider(List<UserDetailsService> userDetailsServices) {
        this.userDetailsServices = userDetailsServices;
    }

    public Member getMember(UserDetailsRequest request) {
        for (UserDetailsService userDetailsService : userDetailsServices) {
            Member member = userDetailsService.getMember(request);
            if (member != null) {
                return member;
            }
        }
        throw new IllegalArgumentException("요청에 적합한 UserDetailsService가 없습니다.");
    }
}
