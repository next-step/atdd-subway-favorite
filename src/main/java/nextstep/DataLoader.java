package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(Member.createAdmin("parkuram12@gmail.com", "pass", 25));
        memberRepository.save(Member.createAdmin("pairpapa26@gmail.com", "pass", 26));
        memberRepository.save(Member.createUser("user1@gmail.com", "user1", 22));
        memberRepository.save(Member.createUser("user2@gmail.com", "user3", 23));
        memberRepository.save(Member.createUser("user3@gmail.com", "user4", 24));
        memberRepository.save(Member.createSubscription("subscription1@gmail.com", "user5", 24));
        memberRepository.save(Member.createSubscription("subscription2@gmail.com", "user6", 24));
    }
}
