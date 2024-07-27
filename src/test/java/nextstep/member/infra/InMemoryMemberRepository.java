package nextstep.member.infra;

import java.util.HashMap;
import java.util.Optional;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

public class InMemoryMemberRepository implements MemberRepository {
    private HashMap<Long, Member> members = new HashMap<>();

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.values()
            .stream()
            .filter(member -> member.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public void deleteByEmail(String email) {
        members.values()
            .removeIf(member -> member.getEmail().equals(email));
    }

    @Override
    public Member save(Member member) {
        members.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public void deleteById(Long id) {
        members.remove(id);
    }
}
