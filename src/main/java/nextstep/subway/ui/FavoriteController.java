package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {

    private final FavoriteService favoritesService;

    public FavoriteController(FavoriteService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest favoriteRequest,
                                                           @AuthenticationPrincipal LoginMember loginMember){

        return ResponseEntity.created(URI.create("/favorites/"+favoritesService.createFavorite(loginMember,favoriteRequest).getId())).build();
    }
}
