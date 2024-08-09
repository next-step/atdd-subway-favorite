package nextstep.member.application;

import nextstep.common.ErrorMessage;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.common.MemberErrorMessage;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.NoMemberExistException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private static final String OAUTH_DEFAULT_PASSWORD = "defaultPassword";

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = findById(id);
        member.update(param.toMember());
    }

    private Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoMemberExistException(MemberErrorMessage.NOT_EXIST_MEMBER));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return findByEmail(email);
    }

    public MemberResponse findMe(String email) {
        Member member = findByEmail(email);
        return MemberResponse.of(member);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoMemberExistException(MemberErrorMessage.NOT_EXIST_MEMBER));
    }

    public Member findMemberByUserResource(String email, int age) {
        try {
            return findMemberByEmail(email);
        } catch (NoMemberExistException exception) {
            createMember(new MemberRequest(email, OAUTH_DEFAULT_PASSWORD, age));
            return findMemberByEmail(email);
        } catch (RuntimeException exception) {
            throw new RuntimeException(ErrorMessage.SERVER_ERROR.getMessage());
        }
    }
}