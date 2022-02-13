package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteSaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<URI> createFavorite(@AuthenticationPrincipal final LoginMember loginMember,
                                              @RequestBody final FavoriteSaveRequest request) {

        final Long favoriteId = favoriteService.createFavorite(loginMember.getId(), request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }
}
