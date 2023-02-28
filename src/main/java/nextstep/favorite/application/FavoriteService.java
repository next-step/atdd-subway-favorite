package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import static nextstep.common.constants.ErrorConstant.NOT_FOUND_STATION;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository,
                           final MemberService memberService,
                           final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }


    public Favorite saveFavorite(final String email, final FavoriteRequest favoriteRequest) {
        final Member member = memberService.findByEmail(email);
        return favoriteRepository.save(createFavoriteEntity(member, favoriteRequest));
    }

    private Favorite createFavoriteEntity(Member member, FavoriteRequest favoriteRequest) {
        try {
            final Station source = stationService.findById(favoriteRequest.getSource());
            final Station target = stationService.findById(favoriteRequest.getTarget());

            return new Favorite(member, source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(NOT_FOUND_STATION);
        }
    }
}
