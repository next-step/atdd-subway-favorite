package nextstep.member.domain;

import java.util.Optional;
import nextstep.member.exception.MemberNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    default Member findByEmailOrElseThrow(String email) {
        return findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException(email));
    }

    void deleteByEmail(String email);
}
