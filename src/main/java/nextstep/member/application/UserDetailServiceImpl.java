package nextstep.member.application;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.application.dto.GithubMemberRequest;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public UserDetailServiceImpl(MemberRepository memberRepository, MemberService memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Override
    public Member findMemberByEmailOrElseThrow(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public void createGithubMember(GithubMemberRequest githubMemberRequest) {
        memberService.createMember(MemberRequest.of(githubMemberRequest));
    }

}
