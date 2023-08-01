package nextstep.api.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.api.favorite.application.dto.FavoriteRequest;
import nextstep.api.favorite.application.dto.FavoriteResponse;
import nextstep.api.favorite.domain.Favorite;
import nextstep.api.favorite.domain.FavoriteRepository;
import nextstep.api.member.domain.MemberRepository;
import nextstep.api.subway.applicaion.path.PathService;
import nextstep.api.subway.domain.station.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final PathService pathService;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    @Transactional
    public FavoriteResponse saveFavorite(final String email, final FavoriteRequest request) {
        final var member = memberRepository.getByEmail(email);

        final var sourceStation = stationRepository.getById(request.getSourceId());
        final var targetStation = stationRepository.getById(request.getTargetId());

        pathService.validateConnected(request.getSourceId(), request.getTargetId());

        final var favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return FavoriteResponse.toResponse(favorite);
    }

    @Transactional
    public void deleteFavorite(final String email, final Long id) {
        final var member = memberRepository.getByEmail(email);
        favoriteRepository.deleteByIdAndMember(id, member);
    }

    public List<FavoriteResponse> findAllFavorites(final String email) {
        final var member = memberRepository.getByEmail(email);

        final var favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }
}
