package nextstep.favorite.ui;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> addFavorites(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        Long favoriteId = favoriteService.addFavorite(loginMember.getId(), request);
        assert favoriteId != null;
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }
}
