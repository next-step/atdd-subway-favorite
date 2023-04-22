package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.exception.InvalidSigninInformation;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;


    public Member signIn(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.
                getEmail()).orElseThrow(() -> new InvalidSigninInformation("email", "가입된 이메일이 존재하지 않습니다."));
        if (!member.getPassword().equals(tokenRequest.getPassword())) {
            throw new InvalidSigninInformation("password", "비밀번호가 올바르지 않습니다.");
        }
        return member;
    }
}
