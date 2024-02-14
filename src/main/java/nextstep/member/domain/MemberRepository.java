package nextstep.member.domain;

import nextstep.exception.ApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    default Member getBy(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new ApplicationException(
                        "사용자를 찾을 수 없습니다. email=" + email));
    }

    void deleteByEmail(String email);
}
