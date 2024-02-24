package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/favorites")
    public Long createFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody FavoriteRequest request
    ) {
        return favoriteService.createFavorite(request, loginMember);
    }

    @GetMapping("/favorites")
    public List<FavoriteResponse> getFavorites() {
        return favoriteService.findFavorites();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/favorites/{id}")
    public void deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
    }
}
