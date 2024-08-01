package nextstep.member.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member createMember(MemberCommand.CreateMember command) {
        return memberRepository.save(command.toMember());
    }

    public void updateMember(MemberCommand.UpdateMember command) {
        Member member = memberRepository.findById(command.getId()).orElseThrow(RuntimeException::new);
        member.update(command.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}