package nextstep.member.application;

import java.util.List;
import nextstep.auth.BadRequestException;
import nextstep.member.domain.FavoriteData;
import nextstep.member.domain.FavoriteDataRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FavoriteDao {

    private final FavoriteDataRepository favoriteDataRepository;
    private final MemberRepository memberRepository;

    public FavoriteDao(FavoriteDataRepository favoriteDataRepository, MemberRepository memberRepository) {
        this.favoriteDataRepository = favoriteDataRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoriteData> getAll(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(BadRequestException::new);
        return favoriteDataRepository.findAllByMember(member);
    }
}
