package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.UserDetail;

public interface UserDetailsService {
	UserDetail loadUserByUsername(String email);
}
