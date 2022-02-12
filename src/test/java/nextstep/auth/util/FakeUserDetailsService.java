package nextstep.auth.util;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

public class FakeUserDetailsService implements UserDetailsService {

    // 가짜 객체 생성시 필요한 데이터
    // 앞서 리뷰주신 내용중에 '제어할 수 없는 외부 요소'에 대해서
    // Repository(DB)로부터 값을 반환하는 로직에 대해서 이렇게 패스워드를 입력받아서 처리해볼 수 있겠다는 장점이 보이네요!
    private final String password;

    public FakeUserDetailsService(final String password) {
        this.password = password;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) {
        if (Objects.isNull(email)) {
            throw new EntityNotFoundException();
        }
        return new FakeUserDetails(password);
    }
}
