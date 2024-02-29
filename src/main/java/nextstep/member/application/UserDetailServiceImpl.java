package nextstep.member.application;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.UserDetail;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public UserDetailServiceImpl(MemberRepository memberRepository, MemberService memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Override
    public UserDetail getUser(String email) {
        Member member = memberService.findMemberByEmail(email);

        return new UserDetail(member.getEmail(), member.getPassword(), member.getAge());
    }

    @Override
    public UserDetail getGithubUser(String email, Integer age) {
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, "github-password", age)));

        return new UserDetail(member.getEmail(), member.getPassword(), member.getAge());
    }
}
