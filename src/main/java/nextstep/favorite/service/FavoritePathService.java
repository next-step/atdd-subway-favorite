package nextstep.favorite.service;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.domain.FavoritePath;
import nextstep.favorite.domain.FavoritePathRepository;
import nextstep.favorite.exception.StationFavoriteCreateFailException;
import nextstep.favorite.service.dto.FavoritePathRequest;
import nextstep.favorite.service.dto.FavoritePathResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.exception.StationLineSearchFailException;
import nextstep.subway.service.StationPathService;
import nextstep.subway.service.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FavoritePathService {
    private final FavoritePathRepository favoritePathRepository;
    private final MemberRepository memberRepository;
    private final StationPathService stationPathService;
    private final StationRepository stationRepository;

    @Transactional
    public Long createFavoritePath(String email, FavoritePathRequest request) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("member entity not found"));

        final Long sourceStationId = request.getSource();
        final Long targetStationId = request.getTarget();

        try {
            stationPathService.searchStationPath(sourceStationId, targetStationId);
        } catch (StationLineSearchFailException exception) {
            throw new StationFavoriteCreateFailException("station favorite create failed because path not exists");
        }

        final FavoritePath favoritePath = FavoritePath.builder()
                .member(member)
                .sourceId(sourceStationId)
                .targetId(targetStationId)
                .build();

        final FavoritePath savedFavoritePath = favoritePathRepository.save(favoritePath);

        return savedFavoritePath.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoritePathResponse> getFavoritePaths(String email) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("member entity not found"));

        final List<FavoritePath> favoritePaths = favoritePathRepository.findAllByMember(member);

        final Map<Long, StationResponse> stationMap = getStationMap(favoritePaths);

        return favoritePaths.stream()
                .map(favoritePath -> FavoritePathResponse.builder()
                        .id(favoritePath.getId())
                        .source(stationMap.get(favoritePath.getSourceId()))
                        .target(stationMap.get(favoritePath.getTargetId()))
                        .build())
                .collect(Collectors.toList());
    }

    private Map<Long, StationResponse> getStationMap(List<FavoritePath> favoritePaths) {
        if (CollectionUtils.isEmpty(favoritePaths)) {
            return Collections.emptyMap();
        }

        final List<Long> stationIds = favoritePaths.stream()
                .flatMap(favoritePath -> Stream.of(favoritePath.getSourceId(), favoritePath.getTargetId()))
                .collect(Collectors.toList());

        final List<Station> stations = stationRepository.findAllById(stationIds);

        return stations.stream()
                .map(StationResponse::fromEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(StationResponse::getId, Function.identity()));
    }

    @Transactional
    public void deleteFavoritePath(Long favoritePathId, String email) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("member entity not found"));

        final FavoritePath favoritePath = favoritePathRepository.findByIdAndMember(favoritePathId, member)
                .orElseThrow(() -> new EntityNotFoundException("favorite path not found"));

        favoritePathRepository.delete(favoritePath);
    }
}
