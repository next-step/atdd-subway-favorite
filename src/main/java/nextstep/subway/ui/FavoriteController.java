package nextstep.subway.ui;

import nextstep.auth.domain.LoginUser;
import nextstep.auth.domain.LoginUserInfo;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("")
    public ResponseEntity<FavoriteResponse> createLine(@LoginUser final LoginUserInfo loginUser, @RequestBody @Valid final FavoriteRequest favoriteRequest) {
        final FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginUser, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<FavoriteResponse> findLineById(@LoginUser final LoginUserInfo loginUser, @PathVariable final Long id) {
        return ResponseEntity.ok(favoriteService.showFavorite(loginUser, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLine(@LoginUser final LoginUserInfo loginUser, @PathVariable Long id) {
        favoriteService.removeFavorite(loginUser, id);
        return ResponseEntity.noContent().build();
    }
}
