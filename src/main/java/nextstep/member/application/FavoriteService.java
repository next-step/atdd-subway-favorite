package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;
    private final LineService lineService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            StationService stationService,
            MemberService memberService,
            LineService lineService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
        this.lineService = lineService;
    }


    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());

        checkConnectedStations(upStation, downStation);

        Member member = memberService.findById(memberId);

        Favorite favorite = favoriteRepository.save(new Favorite(upStation, downStation, member));
        return FavoriteResponse.of(favorite);
    }

    private void checkConnectedStations(Station upStation, Station downStation) {
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.checkConnected(upStation, downStation);
    }
}
