package nextstep.member.repository;

import nextstep.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);

    @Query(value = "select count(m.id) > 0 from member m where m.email = :email limit 1", nativeQuery = true)
    boolean existsByEmail(String email);

}
