package nextstep.member.domain;

import nextstep.member.domain.exception.MemberNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    default Member findByEmailOrFail(String email) {
        return findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    Optional<Member> findByEmail(String email);
    void deleteByEmail(String email);
}
