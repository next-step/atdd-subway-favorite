package nextstep.api.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.api.member.domain.exception.NoSuchMemberException;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(final String email);

    void deleteByEmail(final String email);

    default Member getByEmail(final String email) throws NoSuchMemberException {
        return findByEmail(email).orElseThrow(() -> NoSuchMemberException.from(email));
    }
}
