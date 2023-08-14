package nextstep.member.application;

import java.util.ArrayList;
import java.util.List;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final PathService pathService;
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(PathService pathService,
            MemberService memberService,
            StationService stationService,
            FavoriteRepository favoriteRepository) {
        this.pathService = pathService;
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite createFavorite(String email, FavoriteRequest request) {
        Long sourceId = request.getSource();
        Long targetId = request.getTarget();
        Member member = memberService.getMemberByEmail(email);
        pathService.findPath(sourceId, targetId);
        Favorite favorite = new Favorite(member.getId(), sourceId, targetId);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> getFavorites(String email) {
        List<Favorite> favoriteList = favoriteRepository.findAllByMemberEmail(email);
        return getFavoriteResponse(favoriteList);
    }

    public void deleteFavorite(String email, Long id) {
        Favorite favorite = favoriteRepository.findByIdAndMemberEmail(email, id)
                .orElseThrow(IllegalArgumentException::new);
        favoriteRepository.delete(favorite);
    }

    private List<FavoriteResponse> getFavoriteResponse(List<Favorite> favoriteList) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            StationResponse sourceResponse = getStationResponse(favorite.getSourceId());
            StationResponse targetResponse = getStationResponse(favorite.getTargetId());
            FavoriteResponse response = new FavoriteResponse(favorite.getId(), sourceResponse, targetResponse);
            favoriteResponses.add(response);
        }
        return favoriteResponses;
    }

    private StationResponse getStationResponse(Long stationId) {
        Station station = stationService.findById(stationId);
        return new StationResponse(station.getId(), station.getName());
    }
}
