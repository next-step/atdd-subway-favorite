package nextstep.subway.favorite.application;

import nextstep.subway.favorite.application.exceptions.AccessViolationException;
import nextstep.subway.favorite.application.exceptions.FavoriteNotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public void createFavorite(FavoriteRequest request, Long memberId) {
        final Optional<Member> maybeMember = memberRepository.findById(memberId);
        if (maybeMember.isPresent()) {
            final Member member = maybeMember.get();
            final Favorite favorite = new Favorite(request.getSource(), request.getTarget(), member);
            favoriteRepository.save(favorite);
        }
    }

    @Transactional
    public void deleteFavorite(Long id, Long requestUserId) {
        final Favorite favorite = favoriteRepository
                .findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException("No such data on database!"));

        final Member member = memberRepository.findById(requestUserId)
                .orElseThrow(() -> new AccessViolationException("You have no right to remove favorite."));

        member.removeFavorite(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        final Optional<Member> maybeMember = memberRepository.findById(id);
        if (maybeMember.isEmpty()) {
            return new ArrayList<>();
        }

        final Member member = maybeMember.get();
        final List<Favorite> favorites = member.getFavorites();
        final Map<Long, Station> stations = extractStations(favorites);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    private Map<Long, Station> extractStations(List<Favorite> favorites) {
        Set<Long> stationIds = extractStationIds(favorites);
        return stationRepository.findAllById(stationIds).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Long> extractStationIds(List<Favorite> favorites) {
        Set<Long> stationIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getSourceStationId());
            stationIds.add(favorite.getTargetStationId());
        }
        return stationIds;
    }
}
