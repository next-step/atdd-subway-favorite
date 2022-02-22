package nextstep.member.domain;

import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberByEmail(String email);
}
