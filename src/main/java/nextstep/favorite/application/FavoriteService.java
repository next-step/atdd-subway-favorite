package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;

        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(String email, Long departureStationId, Long destinationStationId) {
        Member member = memberService.findByEmail(email);
        Station departureStation = stationService.findById(departureStationId);
        Station destinationStation = stationService.findById(destinationStationId);

        Favorite saveFavorite = favoriteRepository.save(Favorite.of(member.getId(), departureStation.getId(), destinationStation.getId()));
        return FavoriteResponse.of(saveFavorite, departureStation, destinationStation);
    }

    public List<FavoriteResponse> findFavorites(String email) {
        Member member = memberService.findByEmail(email);
        return favoriteRepository.findAllByMemberId(member.getId())
                .map(this::makeFavoritesResponse)
                .orElseThrow(() -> new FavoriteNotFoundException(member.getId()));
    }

    private List<FavoriteResponse> makeFavoritesResponse(List<Favorite> favorites) {
        return favorites.stream()
                .map(favorite -> FavoriteResponse.of(favorite, stationService.findById(favorite.getDepartureStationId()), stationService.findById(favorite.getDestinationStationId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(String email, Long favoriteId) {
        Member member = memberService.findByEmail(email);
        favoriteRepository.deleteByIdAndMemberId(favoriteId, member.getId());
    }
}
