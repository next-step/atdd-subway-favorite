package nextstep.member.application;

import java.util.ArrayList;
import java.util.List;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.FavoriteStations;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
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

    public List<FavoriteResponse> getFavorites(String email) {
        List<Favorite> favoriteList = favoriteRepository.findAllByMemberEmail(email);
        return getFavoriteResponse(favoriteList);
    }

    private List<FavoriteResponse> getFavoriteResponse(List<Favorite> favoriteList) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            StationResponse sourceResponse = getStationResponse(favorite.getSourceStation());
            StationResponse targetResponse = getStationResponse(favorite.getTargetStation());
            FavoriteResponse response = new FavoriteResponse(favorite.getId(), sourceResponse, targetResponse);
            favoriteResponses.add(response);
        }
        return favoriteResponses;
    }

    private StationResponse getStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
