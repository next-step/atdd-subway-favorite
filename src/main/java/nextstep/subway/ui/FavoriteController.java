package nextstep.subway.ui;

import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

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
}
