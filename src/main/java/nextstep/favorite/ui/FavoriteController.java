package nextstep.favorite.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody FavoriteRequest favoriteRequest){
        FavoriteResponse favorite = favoriteService.createFavorite(userPrincipal.getUsername(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites"+favorite.getId())).body(favorite);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<FavoriteResponse> favorites = favoriteService.findFavorite(userPrincipal.getUsername());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Object> deleteFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long favoriteId){
        favoriteService.deleteFavorites(userPrincipal.getUsername(),favoriteId);
        return ResponseEntity.noContent().build();
    }

}
