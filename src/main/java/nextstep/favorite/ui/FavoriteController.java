package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.favorite.applicataion.FavoriteService;
import nextstep.favorite.applicataion.dto.FavoriteRequest;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<URI> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                                 @RequestBody FavoriteRequest request) {
        favoriteService.createFavorite(loginMember.getId(), request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create("/favorites")).build();

    }

}
