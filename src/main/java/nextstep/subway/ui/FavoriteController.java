package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.Authentication;
import nextstep.auth.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> addFavorite(@Authentication LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        final var favoriteResponse = favoriteService.addFavorite(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@Authentication LoginMember loginMember) {
        return ResponseEntity.ok().body(favoriteService.findFavorites(loginMember));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void removeFavorite(@Authentication LoginMember loginMember, @PathVariable Long id) {
        favoriteService.removeFavorite(loginMember, id);
    }

}
