package nextstep.member.application.service;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceGateway implements CommandService<MemberResponse>, QueryService<MemberResponse> {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    public MemberServiceGateway(MemberCommandService memberCommandService, MemberQueryService memberQueryService) {
        this.memberCommandService = memberCommandService;
        this.memberQueryService = memberQueryService;
    }

    @Override
    public MemberResponse create(MemberRequest request) {
        return memberCommandService.createMember(request);
    }

    @Override
    public void update(String email, MemberRequest param) {
        memberCommandService.updateMember(email, param);
    }

    @Override
    public void update(Long id, MemberRequest param) {
        memberCommandService.updateMember(id, param);
    }

    @Override
    public MemberResponse find(Long id) {
        return memberQueryService.findMember(id);
    }

    @Override
    public MemberResponse find(String email) {
        return memberQueryService.findMember(email);
    }

    @Override
    public void delete(Long id) {
        memberCommandService.deleteMember(id);
    }

    @Override
    public void delete(String email) {
        memberCommandService.deleteMember(email);
    }
}
