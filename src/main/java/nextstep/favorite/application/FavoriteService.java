package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteNotFoundException;
import nextstep.favorite.exception.InvalidFavoriteOwnerException;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(String email, FavoriteRequest favoriteRequest) {
        pathService.validateDuplicatedStations(favoriteRequest.getSource(), favoriteRequest.getTarget());
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());

        pathService.validatePath(favoriteRequest.getSource(), favoriteRequest.getTarget());

        Long memberId = getMemberIdByEmail(email);
        Favorite favorite = new Favorite(memberId, source, target);
        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public FavoriteResponse findFavorite(String email, Long id) {
        Favorite favorite = findById(id);

        Long memberId = getMemberIdByEmail(email);
        validateOwner(favorite, memberId);

        return FavoriteResponse.of(favorite);
    }

    @Transactional
    public void deleteFavorite(String email, Long id) {
        Favorite favorite = findById(id);

        Long memberId = getMemberIdByEmail(email);
        validateOwner(favorite, memberId);

        favoriteRepository.delete(favorite);
    }

    private void validateOwner(Favorite favorite, Long memberId) {
        if (favorite.isNotOwner(memberId)) {
            throw new InvalidFavoriteOwnerException();
        }
    }

    private Long getMemberIdByEmail(String email) {
        return memberService.findMember(email).getId();
    }

    private Favorite findById(Long id) {
        return favoriteRepository.findWithById(id)
                .orElseThrow(FavoriteNotFoundException::new);
    }

}
