package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.dto.FavoriteCreateRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Long saveFavorite(String email, FavoriteCreateRequest favoriteCreateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Long source = favoriteCreateRequest.getSource();
        Long target = favoriteCreateRequest.getTarget();
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return favorite.getId();
    }
}
