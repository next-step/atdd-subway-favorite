package nextstep.favorite.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteSimpleResponse;
import nextstep.favorite.application.exception.FavoriteException.NotCreatedException;
import nextstep.favorite.application.exception.FavoriteException.NotFoundException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.SectionEdges;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.exception.StationException.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        MemberRepository memberRepository, StationRepository stationRepository,
        LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public FavoriteSimpleResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(
            NotCreatedException::new);
        validatePath(request.getSource(), request.getTarget());
        Favorite favorite = new Favorite(member.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
        return new FavoriteSimpleResponse(favorite.getId());
    }

    private void validatePath(Long sourceId, Long targetId) {
        Station source = stationRepository.findById(sourceId)
            .orElseThrow(StationNotFoundException::new);
        Station target = stationRepository.findById(targetId)
            .orElseThrow(StationNotFoundException::new);
        List<Line> lines = lineRepository.findAll();
        SectionEdges edges = new SectionEdges(lines);
        PathFinder pathFinder = new PathFinder(edges);
        pathFinder.findShortedPath(source.getId(), target.getId());
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
            .orElseThrow(NotCreatedException::new);
        Map<Long, Station> stationIdMap = getAllStationsInLinesMap();

        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return favorites.stream().map(favorite -> getFavoriteResponse(stationIdMap, favorite))
            .collect(Collectors.toList());
    }

    private Map<Long, Station> getAllStationsInLinesMap() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
            .flatMap(line -> line.getStations().stream())
            .collect(Collectors.toSet()).stream()
            .collect(Collectors.toMap(Station::getId, station -> station));
    }

    private static FavoriteResponse getFavoriteResponse(Map<Long, Station> stationIdMap,
        Favorite favorite) {
        Station source = stationIdMap.get(favorite.getSource());
        Station target = stationIdMap.get(favorite.getTarget());
        return new FavoriteResponse(favorite.getId(),
            new StationResponse(source.getId(), source.getName()),
            new StationResponse(target.getId(), target.getName()));
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
            .orElseThrow(NotCreatedException::new);
        Favorite favorite = favoriteRepository.findByIdAndMemberId(id, member.getId())
            .orElseThrow(NotFoundException::new);
        favoriteRepository.delete(favorite);
    }
}
