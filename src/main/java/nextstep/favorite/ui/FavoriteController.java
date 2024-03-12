package nextstep.favorite.ui;

import nextstep.auth.domain.LoginUserDetail;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteDeleteCommand;
import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.favorite.application.dto.FavoriteFindQuery;
import nextstep.favorite.ui.dto.FavoriteRequest;
import nextstep.favorite.ui.dto.FavoriteResponse;
import nextstep.auth.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(
            @AuthenticationPrincipal LoginUserDetail loginUserDetail,
            @RequestBody FavoriteRequest request
    ) {
        FavoriteDto favorite = favoriteService.createFavorite(FavoriteRequest.toCommand(request, loginUserDetail));
        return ResponseEntity.created(URI.create("/favorites" + favorite.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginUserDetail loginUserDetail) {
        List<FavoriteDto> favorites = favoriteService.findFavorites(new FavoriteFindQuery(loginUserDetail.getEmail()));
        return ResponseEntity.ok().body(
                favorites.stream()
                        .map(FavoriteResponse::from)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(
            @AuthenticationPrincipal LoginUserDetail loginUserDetail, @PathVariable Long id
    ) {
        favoriteService.deleteFavorite(new FavoriteDeleteCommand(id, loginUserDetail.getEmail()));
        return ResponseEntity.noContent().build();
    }
}
