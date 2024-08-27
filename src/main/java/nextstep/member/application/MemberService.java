package nextstep.member.application;

import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.authentication.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = save(request);
        return MemberResponse.of(member);
    }

    @Transactional
    public Member save(MemberRequest request) {
        return memberRepository.save(request.toMember());
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(MemberResponse::of)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public Member lookUpOrCreateMember(GithubProfileResponse githubProfileResponse) {
        Member member;
        try {
            member = findMemberByEmail(githubProfileResponse.getEmail());
        } catch (MemberNotFoundException exception) {
            member = save(MemberRequest.of(githubProfileResponse.getEmail(), githubProfileResponse.getAge()));
        }
        return member;
    }
}