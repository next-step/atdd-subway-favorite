package nextstep.favorite.application;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.global.exception.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.section.SectionRepository;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = getMember(loginMember);

        checkRegisted(member.getId(), request.getSource(), request.getTarget());
        validateStation(request.getSource());
        validateStation(request.getTarget());
        validatePath(request.getSource(), request.getTarget());

        Favorite favorite = new Favorite(member.getId(), request.getSource(), request.getTarget());
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return savedFavorite.getId();
    }

    private void checkRegisted(Long memberId, Long sourceId, Long targetId) {
        Optional<Favorite> favorite = favoriteRepository.findByMemberIdAndSourceIdAndTargetId(memberId, sourceId, targetId);
        if (favorite.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 즐겨찾기입니다.");
        }
    }

    private void validateStation(Long stationId) {
        stationRepository.findById(stationId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 지하철입니다.")
        );
    }

    private void validatePath(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalArgumentException("출발역과 종착역이 같습니다.");
        }

        List<Section> sections = sectionRepository.findAll();
        SubwayGraph subwayGraph = new SubwayGraph(sections);
        DijkstraShortestPath shortestPath = subwayGraph.getShortestPath();
        try {
            shortestPath.getPath(sourceId, targetId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역과 종착역이 연결되어 있지 않습니다.");
        }
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(
                AuthenticationException::new
        );
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = getMember(loginMember);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        Set<Long> stationIds = getStationIds(favorites);
        Map<Long, Station> stationMap = getStationMap(stationIds);

        return favorites.stream()
                .map(favorite -> getFavoriteResponse(favorite, stationMap))
                .collect(Collectors.toList());
    }

    private Map<Long, Station> getStationMap(Set<Long> stationIds) {
        return stationRepository.findAllById(stationIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, station -> station));
    }

    private Set<Long> getStationIds(List<Favorite> favorites) {
        return favorites.stream()
                .flatMap(favorite -> Stream.of(favorite.getSourceId(), favorite.getTargetId()))
                .collect(Collectors.toSet());
    }

    private FavoriteResponse getFavoriteResponse(Favorite favorite, Map<Long, Station> stationMap) {
        StationResponse sourceResponse = getStationResponse(stationMap.get(favorite.getSourceId()));
        StationResponse targetResponse = getStationResponse(stationMap.get(favorite.getTargetId()));
        return FavoriteResponse.of(favorite, sourceResponse, targetResponse);
    }

    private StationResponse getStationResponse(Station station) {
        return StationResponse.of(station);
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = getMember(loginMember);
        checkPresent(id);
        checkCreatedMember(id, member);
        favoriteRepository.deleteById(id);
    }

    private void checkPresent(Long id) {
        Optional<Favorite> favorite = favoriteRepository.findById(id);
        if (!favorite.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 즐겨찾기입니다.");
        }
    }

    private void checkCreatedMember(Long id, Member member) {
        Optional<Favorite> favorite = favoriteRepository.findByIdAndMemberId(id, member.getId());
        if (!favorite.isPresent()) {
            throw new AuthenticationException();
        }
    }
}
