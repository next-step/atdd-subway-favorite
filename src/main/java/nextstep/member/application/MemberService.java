package nextstep.member.application;

import nextstep.exception.ApiException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
        Member member = findMemberById(id);
        return MemberResponse.of(member);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Member validateMemberAndReturn(TokenRequest request) {
        Member member = findMemberByEmail(request.getEmail());
        if (member.checkPassword(request.getPassword())) {
            return member;
        }
        throw new ApiException(HttpStatus.UNAUTHORIZED, "회원정보가 일치하지 않습니다.");
    }

    public MemberResponse findByEmail(String email) {
        return MemberResponse.of(findMemberByEmail(email));
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
            () -> new ApiException(HttpStatus.NOT_FOUND, "회원정보가 존재하지 않습니다.")
        );
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = findMemberById(id);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}