package nextstep.subway.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    Optional<Member> findByEmail(String email);
}
