package nextstep.favorite.ui;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
import nextstep.member.domain.AuthToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/favorites")
@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> createFavorite(
            @AuthToken String email,
            @RequestBody FavoriteCreateRequest favoriteCreateRequest) {

        Long id = favoriteService.createFavorite(email, favoriteCreateRequest);

        return ResponseEntity.created(
                URI.create("/favorites/" + id)
        ).build();
    }
}
