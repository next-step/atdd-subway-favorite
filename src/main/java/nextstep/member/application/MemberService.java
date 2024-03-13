package nextstep.member.application;

import nextstep.exception.BadRequestException;
import nextstep.member.application.dto.MemberInfoRequest;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(BadRequestException.MEMBER_NOT_FOUND));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(BadRequestException.MEMBER_NOT_FOUND));
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new BadRequestException(BadRequestException.MEMBER_NOT_FOUND));
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
            .map(MemberResponse::of)
            .orElseThrow(RuntimeException::new);
    }

    public MemberResponse findMember(MemberInfoRequest request) {
        Member member = findMemberByEmail(request.getEmail());
        if (request.getPassword() == null) {
            // OAuth2 벤더를 통해서 로그인 한 경우이므로, 벤더 체크 후 회원 정보 반환
            return MemberResponse.of(member);
        }

        if (!member.checkPassword(request.getPassword())) {
            throw new BadRequestException(BadRequestException.MEMBER_PASSWORD_MISMATCH);
        }
        return MemberResponse.of(member);
    }
}
