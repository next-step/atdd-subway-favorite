package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteStationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.NotFavoriteOwnerException;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberService memberService, final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(final String email, final FavoriteRequest favoriteRequest) {
        final Long memberId = memberService.findMember(email).getId();

        final Favorite favorite = favoriteRequest.toFavorite(memberId);
        final Favorite savedFavorite = favoriteRepository.save(favorite);

        return createFavoriteResponse(savedFavorite);
    }

    private FavoriteResponse createFavoriteResponse(final Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                new FavoriteStationResponse(stationService.findById(favorite.getSource())),
                new FavoriteStationResponse(stationService.findById(favorite.getTarget())));
    }

    public List<FavoriteResponse> findFavorites(final String email) {
        final Long memberId = memberService.findMember(email).getId();

        return favoriteRepository.findAllByMemberId(memberId)
                .stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    public FavoriteResponse findFavorite(final String email, final Long id) {
        final Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow();

        final Long memberId = memberService.findMember(email).getId();

        if (!favorite.isOwner(memberId)) {
            throw new NotFavoriteOwnerException();
        }

        return createFavoriteResponse(favorite);
    }

    @Transactional
    public void deleteFavorite(final String email, final Long id) {
        final Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow();

        final Long memberId = memberService.findMember(email).getId();

        if (!favorite.isOwner(memberId)) {
            throw new NotFavoriteOwnerException();
        }

        favoriteRepository.deleteById(id);
    }

}
