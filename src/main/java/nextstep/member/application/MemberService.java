package nextstep.member.application;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.LoginUserDetail;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.dto.MemberDto;
import nextstep.member.domain.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberDto.of(member);
    }

    public MemberDto findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberDto.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public MemberDto findByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        return MemberDto.of(member);
    }

    @Override
    public MemberDto findByEmailOrCreate(String email) {
        Member member = memberRepository
                .findByEmail(email)
                .orElse(new Member(email, null, null));
        memberRepository.save(member);

        return MemberDto.of(member);
    }

    public MemberDto findMe(LoginUserDetail loginUserDetail) {
        return memberRepository.findByEmail(loginUserDetail.getEmail())
                .map(MemberDto::of)
                .orElseThrow(RuntimeException::new);
    }
}
