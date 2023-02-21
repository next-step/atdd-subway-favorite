package nextstep.member.domain;

import nextstep.member.application.dto.MemberResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);

    void deleteByEmail(String email);

    Optional<MemberResponse> findByEmail(String email);
}
