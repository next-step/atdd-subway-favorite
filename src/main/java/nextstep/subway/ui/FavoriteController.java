package nextstep.subway.ui;

import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody final FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(userDetails, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavoriteList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(favoriteService.getFavoriteList(userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> getFavorite(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable("id") final Long favoriteId) {
        return ResponseEntity.ok(favoriteService.getFavorite(userDetails, favoriteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable("id") final Long favoriteId) {
        favoriteService.deleteFavorite(userDetails, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
