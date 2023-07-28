package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.dto.FavoriteCreateRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFoundException;
import nextstep.subway.path.application.PathService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final PathService pathService;

    public FavoriteService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, PathService pathService) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.pathService = pathService;
    }

    public Long saveFavorite(String email, FavoriteCreateRequest favoriteCreateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Long source = favoriteCreateRequest.getSource();
        Long target = favoriteCreateRequest.getTarget();
        pathService.validatePath(source, target);

        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return favorite.getId();
    }
}
