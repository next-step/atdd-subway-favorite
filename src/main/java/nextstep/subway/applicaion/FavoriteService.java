package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.global.error.exception.NotFoundMemberException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationService stationService;
    private final LineService lineService;

    public FavoriteService(FavoriteRepository favoriteRepository,
            MemberRepository memberRepository, StationService stationService,
            LineService lineService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public FavoriteResponse registerFavorites(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(NotFoundMemberException::new);

        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        subwayMap.isStationsConnected(source, target);

        Favorite favorite = new Favorite(member, source, target);
        Favorite save = favoriteRepository.save(favorite);

        return FavoriteResponse.from(save);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(NotFoundMemberException::new);

        List<Favorite> favorites = favoriteRepository.findByMember(member);

        return favorites.stream().map(FavoriteResponse::from)
                                .collect(Collectors.toList());
    }
}
