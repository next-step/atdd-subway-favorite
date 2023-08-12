package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.FavoriteStations;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final PathService pathService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(PathService pathService,
            MemberService memberService,
            FavoriteRepository favoriteRepository) {
        this.pathService = pathService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite createFavorite(String email, FavoriteRequest request) {
        Member member = memberService.getMemberByEmail(email);
        FavoriteStations favoriteStations = pathService.checkValidPathForFavorite(request.getSource(),
                request.getTarget());
        Favorite favorite = new Favorite(member, favoriteStations);
        return favoriteRepository.save(favorite);
    }
}
