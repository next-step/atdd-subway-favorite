package nextstep.subway.member.domain;

import java.util.Optional;

public interface MemberRepository{
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    void deleteById(Long id);

    Member save(Member toMember);
}
