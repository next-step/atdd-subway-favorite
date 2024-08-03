package nextstep.member.domain.repository;

import lombok.NonNull;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.exception.NotFoundMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @NonNull
    default Member findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(NotFoundMemberException::new);
    }

    void deleteByEmail(String email);
}
