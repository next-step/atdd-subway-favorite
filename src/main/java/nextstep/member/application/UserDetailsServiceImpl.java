package nextstep.member.application;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.application.dto.AuthMember;
import nextstep.auth.application.dto.ProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static nextstep.common.constant.ErrorCode.MEMBER_NOT_FOUND;
import static nextstep.converter.MemberConverter.MemberToAuthMember;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberServices memberService;
    private final MemberRepository memberRepository;

    public UserDetailsServiceImpl(MemberServices memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Override
    public AuthMember findAuthMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new MemberException(String.valueOf(MEMBER_NOT_FOUND))
        );
        return MemberToAuthMember(member);
    }

    @Override
    public AuthMember findAuthMemberOrOtherJob(ProfileResponse profileResponse) {
        Optional<Member> memberOptional = memberService.findMemberOptionalByEmail(profileResponse.getEmail());
        if (memberOptional.isPresent()) {
            return MemberToAuthMember(memberOptional.get());
        }

        return createMember(profileResponse);
    }

    private AuthMember createMember(ProfileResponse profileResponse) {
        MemberRequest memberRequest = MemberRequest.of(
                profileResponse.getEmail(),
                "password",
                profileResponse.getAge()
        );

        MemberResponse memberResponse = memberService.createMember(memberRequest);
        return AuthMember.of(memberResponse.getEmail(), null);
    }
}

