package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.FavoriteDto;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberErrorMessage;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.exception.FavoriteOwnerException;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayErrorMessage;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FavoriteProducer {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteDto saveFavorite(String username, FavoriteRequest request) {
        Member findMember = findUserByUsername(username);
        Station target = findStationById(request.getTarget());
        Station source = findStationById(request.getSource());

        Favorite saveFavorite = favoriteRepository.save(Favorite.of(findMember.getId(), target.getId(), source.getId()));
        return FavoriteDto.of(saveFavorite.getId(), target, source);
    }

    public FavoriteDto findFavorite(Long id) {
        Favorite favorite = findById(id);
        Station target = findStationById(favorite.getTargetId());
        Station source = findStationById(favorite.getSourceId());
        return FavoriteDto.of(id, target, source);
    }

    public List<FavoriteDto> findAllByUsername(String username) {
        Member findMember = findUserByUsername(username);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(findMember.getId());
        Map<Long, Station> stations = extractStationsAs(favorites);
        return FavoriteDto.of(favorites, stations);
    }

    private Map<Long, Station> extractStationsAs(List<Favorite> favorites) {
        Set<Long> stationIds = findStationIds(favorites);
        List<Station> stations = stationRepository.findAllById(stationIds);
        return stations.stream().collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Long> findStationIds(List<Favorite> favorites) {
        Set<Long> stationIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getTargetId());
            stationIds.add(favorite.getSourceId());
        }
        return stationIds;
    }

    public void deleteFavorite(Long id) {
        checkExistsFavorite(id);
        favoriteRepository.deleteById(id);
    }

    private Favorite findById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(
                () -> new FavoriteOwnerException(MemberErrorMessage.NOT_FOUND_FAVORITE.getMessage()));
    }

    private Member findUserByUsername(String username) {
        return memberRepository.findByEmail(username).orElseThrow(IllegalArgumentException::new);
    }

    private void checkExistsFavorite(Long id) {
        if (!favoriteRepository.existsById(id)) {
            throw new FavoriteOwnerException(MemberErrorMessage.NOT_FOUND_FAVORITE.getMessage());
        }
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(SubwayErrorMessage.NOT_FOUND_STATION.getMessage())
        );
    }
}
