package nextstep.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);

    Member save(Member member);

    Optional<Member> findById(Long id);

    void deleteById(Long id);
}
