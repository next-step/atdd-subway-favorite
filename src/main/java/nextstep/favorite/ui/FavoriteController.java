package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteDeleteCommand;
import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.favorite.application.dto.FavoriteFindQuery;
import nextstep.favorite.ui.dto.FavoriteRequest;
import nextstep.favorite.ui.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody FavoriteRequest request
    ) {
        FavoriteDto favorite = favoriteService.createFavorite(FavoriteRequest.toCommand(request, loginMember));
        return ResponseEntity.created(URI.create("/favorites" + favorite.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteDto> favorites = favoriteService.findFavorites(new FavoriteFindQuery(loginMember.getEmail()));
        return ResponseEntity.ok().body(
                favorites.stream()
                        .map(FavoriteResponse::from)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id
    ) {
        favoriteService.deleteFavorite(new FavoriteDeleteCommand(id, loginMember.getEmail()));
        return ResponseEntity.noContent().build();
    }
}
