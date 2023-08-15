package nextstep.subway.application;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.application.dto.favorite.FavoriteRequest;
import nextstep.subway.application.dto.favorite.FavoriteResponse;
import nextstep.subway.application.dto.station.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService,
                           MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @Transactional
    public FavoriteResponse createFavorite(String email, FavoriteRequest request) {
        MemberResponse memberResponse = memberService.findMemberByEmail(email);
        Station source = stationService.findStationEntityById(request.getSource());
        Station target = stationService.findStationEntityById(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(memberResponse.getId(), source, target));
        return createFavoriteResponse(favorite);
    }

    public List<FavoriteResponse> findFavorite(String email) {
        MemberResponse memberResponse = memberService.findMemberByEmail(email);
        return favoriteRepository.findAllByMemberId(memberResponse.getId()).stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        StationResponse sourceResponse = createStationResponse(favorite.getSource());
        StationResponse targetResponse = createStationResponse(favorite.getTarget());
        return new FavoriteResponse(favorite.getId(), sourceResponse, targetResponse);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

}