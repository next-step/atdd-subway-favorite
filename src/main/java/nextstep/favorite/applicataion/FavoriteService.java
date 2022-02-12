package nextstep.favorite.applicataion;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
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
    private final MemberService memberService;
    private final StationService stationService;
    private final LineService lineService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService, LineService lineService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public Long createFavorite(Long memberId, Long sourceStationId, Long targetStationId) {
        Member member = memberService.findById(memberId);
        Station sourceStation = stationService.findById(sourceStationId);
        Station targetStation = stationService.findById(targetStationId);

        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.findPath(sourceStation, targetStation);

        Favorite favorite = favoriteRepository.save(Favorite.of(member, sourceStation, targetStation));

        return favorite.getId();
    }
}
