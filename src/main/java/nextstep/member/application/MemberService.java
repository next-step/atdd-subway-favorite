package nextstep.member.application;

import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.exception.NotEqualPasswordException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class MemberService {
    private JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

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

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String password = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 email 을 가지는 member 가 없습니다."))
                .getPassword();
        checkPassword(password, tokenRequest.getPassword());
        return new TokenResponse(jwtTokenProvider.createToken(email));
    }

    private void checkPassword(String password, String passwordRequest) {
        if (password.equals(passwordRequest)) {
            return;
        }
        throw new NotEqualPasswordException();
    }
}