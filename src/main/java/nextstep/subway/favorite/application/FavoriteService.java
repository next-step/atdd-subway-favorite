package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.LoginMemberPrincipal;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.exception.NotExistStationException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private StationService stationService;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite saveFavorite(final LoginMemberPrincipal loginMember, final FavoriteRequest request){
        final Station source = stationService.findStationById(Long.parseLong(request.getSource()));
        final Station target = stationService.findStationById(Long.parseLong(request.getTarget()));
        return favoriteRepository.save(new Favorite(loginMember.getId(), source.getId(), target.getId()));
    }


    public List<FavoriteResponse> getFavorites(final LoginMemberPrincipal loginMember) {
        return favoriteRepository.findByMemberId(loginMember.getId())
                .stream()
                .map(favorite -> convertFavorite(favorite))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(final LoginMemberPrincipal loginMember, Long id) {
        final Favorite favorite = favoriteRepository.findByIdAndMemberId(id, loginMember.getId()).orElseThrow(RuntimeException::new);;
        favoriteRepository.deleteById(favorite.getId());
    }

    private FavoriteResponse convertFavorite(final Favorite favorite){
        final List< StationResponse > stationResponses = stationService.findAllStations();
        final StationResponse source = findStation(stationResponses, favorite.getSourceId());
        final StationResponse target = findStation(stationResponses, favorite.getTargetId());
        return FavoriteResponse.of(favorite, source, target);
    }

     private StationResponse findStation(List< StationResponse > stationResponseList, final Long stationId) {
        return stationResponseList
                .stream()
                .filter(s -> s.getId().equals(stationId))
                .findFirst()
                .orElseThrow(NotExistStationException::new);
    }
}
