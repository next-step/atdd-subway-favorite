package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<FavoriteResponse> showFavorites(final String email) {
        final Member member = memberService.findByEmail(email);
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return createFavoriteResponse(favorites);
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

    private static List<FavoriteResponse> createFavoriteResponse(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(final Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
