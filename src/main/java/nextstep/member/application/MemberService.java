package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final GithubClient githubClient;

    public MemberService(MemberRepository memberRepository, GithubClient githubClient) {
        this.memberRepository = memberRepository;
        this.githubClient = githubClient;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse getGithubProfile(String accessToken) {
        GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(accessToken);
        return new MemberResponse(githubProfileResponse.getEmail(), githubProfileResponse.getAge());
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
