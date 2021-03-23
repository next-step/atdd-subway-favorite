package nextstep.repository;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MemoryMemberRepository implements MemberRepository {
    private Map<Long, Member> members = new HashMap<>();


    @Override
    public Optional<Member> findByEmail(String email) {
        return members.values().stream()
                .filter(member -> email.equals(member.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));

    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(station -> members.remove(id, station));

    }

    @Override
    public Member save(Member entity) {
        boolean isNew = Objects.isNull(entity.getId());
        if (!isNew) {
            return merge(entity);
        }

        checkDuplicateName(entity);

        long id = members.size()+1;
        Member newMember = new Member(id, entity.getEmail(), entity.getPassword(), entity.getAge(), entity.getFavorites());
        members.put(id, newMember);
        return newMember;
    }

    protected void checkDuplicateName(Member entity) {
        members.values().stream()
                .filter(entity::equals)
                .findFirst()
                .ifPresent(line->{throw new RuntimeException();});
    }

    private Member merge(Member entity) {
        Member Member = findById(entity.getId()).orElseThrow(IllegalArgumentException::new);
        Member.update(entity);
        return Member;
    }
}
