package nextstep.member.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteQueryService {
    private final MemberService memberService;

    public FavoriteQueryService(MemberService memberService) {
        this.memberService = memberService;
    }

    public List<Favorite> find(UserPrincipal userPrincipal) {
        Member member = memberService.findMemberByEmail(userPrincipal.getUsername());
        return member.getFavorites();
    }
}
