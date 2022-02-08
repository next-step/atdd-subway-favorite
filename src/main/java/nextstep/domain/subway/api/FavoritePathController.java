package nextstep.domain.subway.api;

import nextstep.auth.authentication.LoginMember;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.domain.subway.dto.FavoritePathRequest;
import nextstep.domain.subway.service.FavoritePathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoritePathController {
    private final FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity<?> createFavoritePath(@AuthenticationPrincipal LoginMember loginMember, FavoritePathRequest favoritePathRequest) {
        long saveFavoriteId = favoritePathService.createFavorite(loginMember, favoritePathRequest);
        return ResponseEntity.created(URI.create("/favorites/" + saveFavoriteId)).build();
    }


}
