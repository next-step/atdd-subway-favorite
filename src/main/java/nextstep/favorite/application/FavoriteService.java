package nextstep.favorite.application;

import nextstep.auth.domain.UserDetail;
import nextstep.exception.NotFoundException;
import nextstep.favorite.application.dto.FavoriteCreateResponse;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.service.StationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.controller.dto.StationResponse.stationToStationResponse;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;
    private LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService, LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public FavoriteCreateResponse createFavorite(UserDetail userDetail, FavoriteRequest request) {
        Member member = memberService.findMemberByEmail(userDetail.getEmail());

        Station sourceStation = stationService.getStationById(request.getSource());
        Station targetStation = stationService.getStationById(request.getTarget());

        validFindPath(lineRepository.findAll(), sourceStation, targetStation);

        Favorite favoriteBuilder = Favorite.builder()
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .member(member)
                .build();
        member.addFavorite(favoriteBuilder);

        Favorite favorite = favoriteRepository.save(favoriteBuilder);
        return FavoriteCreateResponse.builder()
                .id(favorite.getId())
                .build();
    }

    public List<FavoriteResponse> findFavorites(UserDetail userDetail) {
        memberService.findMemberByEmail(userDetail.getEmail());

        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream().map(favorite -> FavoriteResponse.builder()
                .id(favorite.getId())
                .source(stationToStationResponse(favorite.getSourceStation()))
                .target(stationToStationResponse(favorite.getTargetStation()))
                .build()).collect(Collectors.toList());
    }

    public void deleteFavorite(UserDetail userDetail, Long id) {
        memberService.findMemberByEmail(userDetail.getEmail());

        Favorite favorite = findFavoriteById(id);

        favoriteRepository.delete(favorite);
    }

    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 즐겨찾기입니다."));
    }

    private void validFindPath(List<Line> lineList, Station source, Station target) {
        PathFinder pathFinder = new PathFinder(lineList);
        pathFinder.findPath(source, target);
    }
}
