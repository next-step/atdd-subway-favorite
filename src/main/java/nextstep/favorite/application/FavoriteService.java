package nextstep.favorite.application;

import nextstep.exception.NotFoundUserException;
import nextstep.favorite.application.request.AddFavoriteRequest;
import nextstep.favorite.application.response.ShowAllFavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.application.PathFinder;
import nextstep.subway.application.StationService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private LineRepository lineRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, LineRepository lineRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public void createFavorite(LoginMember loginMember, AddFavoriteRequest addFavoriteRequest) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(NotFoundUserException::new);
        List<Line> lines = lineRepository.findAll();
        Station startStation = stationService.findById(addFavoriteRequest.getStartStationId());
        Station endStation = stationService.findById(addFavoriteRequest.getEndStationId());

        PathFinder.findShortestPath(lines, startStation, endStation);

        Favorite favorite = Favorite.of(startStation, endStation);
        member.addFavorite(favorite);
    }

    public ShowAllFavoriteResponse findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(NotFoundUserException::new);
        return ShowAllFavoriteResponse.from(member.getFavorites());
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
