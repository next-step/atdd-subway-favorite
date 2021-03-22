package nextstep.subway.member.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static nextstep.subway.auth.infrastructure.AuthorizationExtractor.ACCESS_TOKEN_TYPE;
import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

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
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberOfMine(LoginMember loginMember) {
        return MemberResponse.of(loginMember);
    }

}