package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService, final StationService stationService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal final LoginMember loginMember,
                                                           @RequestBody final FavoriteRequest favoriteRequest) {
        final FavoriteResponse favorite = favoriteService.saveFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal final LoginMember loginMember) {
        final List<FavoriteResponse> favorites = favoriteService.findAllFavorites(loginMember.getId());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorites(@AuthenticationPrincipal final LoginMember loginMember,
                                                @PathVariable final Long id) {
        favoriteService.deleteFavoriteById(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
