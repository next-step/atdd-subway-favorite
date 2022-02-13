package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favoriteResponseList = favoriteService.findFavorite(loginMember.getId());
        return ResponseEntity.ok().body(favoriteResponseList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

}
