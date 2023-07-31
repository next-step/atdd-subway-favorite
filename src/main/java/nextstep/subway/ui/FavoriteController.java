package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.applicaion.FavoritesService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            .created(
                URI.create("/favorites/" + service.create(request, userPrincipal.getUsername())))
            .build();
    }

    @GetMapping("/favorites")
    ResponseEntity<List<FavoritesResponse>> getFavorite(
        @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(
            service.findAllByUser(userPrincipal.getUsername())
        );
    }
}
