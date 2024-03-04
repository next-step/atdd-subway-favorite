package nextstep.member.application;

import nextstep.exception.BadRequestException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("요청하신 이메일 정보는 정보는 올바르지 않은 정보입니다."));
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(it -> MemberResponse.of(it))
                .orElseThrow(RuntimeException::new);
    }

    public Member findOrCreateMember(String email, int age) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent()) {
            return memberOptional.get();
        }

        return memberRepository.save(Member.builder()
                .email(email)
                .age(age)
                .password(UUID.randomUUID().toString())
                .build());
    }

}