package nextstep.member.application;

import java.util.List;
import nextstep.auth.BadRequestException;
import nextstep.member.domain.FavoriteResponse;
import nextstep.member.domain.FavoriteResponseRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FavoriteDao {

    private final FavoriteResponseRepository favoriteResponseRepository;
    private final MemberRepository memberRepository;

    public FavoriteDao(FavoriteResponseRepository favoriteResponseRepository, MemberRepository memberRepository) {
        this.favoriteResponseRepository = favoriteResponseRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getAll(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(BadRequestException::new);
        return favoriteResponseRepository.findAllByMember(member);
    }
}
