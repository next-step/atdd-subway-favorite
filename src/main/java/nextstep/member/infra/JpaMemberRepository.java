package nextstep.member.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);
}
