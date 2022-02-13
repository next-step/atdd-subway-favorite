package nextstep.auth.model.authentication.service;

import nextstep.auth.model.authentication.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
