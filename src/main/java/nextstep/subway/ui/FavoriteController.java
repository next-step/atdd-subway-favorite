package nextstep.subway.ui;

import java.net.URI;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.applicaion.FavoritesService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoritesService service;

    public FavoriteController(FavoritesService service) {
        this.service = service;
    }

    @PostMapping("/favorites")
    ResponseEntity<Object> addFavorites(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody FavoriteRequest request) {

        return ResponseEntity
            .created(URI.create("/favorites/" + service.create(request, userPrincipal.getUsername())))
            .build();
    }
}
