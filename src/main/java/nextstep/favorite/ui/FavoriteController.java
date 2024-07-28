package nextstep.favorite.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.LoginMember;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
  private final FavoriteService favoriteService;

  @PostMapping
  public ResponseEntity<Void> createFavorite(
      @RequestBody FavoriteRequest request, @AuthenticationPrincipal LoginMember loginMember) {
    FavoriteResponse favorite = favoriteService.createFavorite(request, loginMember);
    return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
  }

  @GetMapping
  public ResponseEntity<List<FavoriteResponse>> getFavorites(
      @AuthenticationPrincipal LoginMember loginMember) {
    List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
    return ResponseEntity.ok().body(favorites);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFavorite(
      @PathVariable Long id, @AuthenticationPrincipal LoginMember loginMember) {
    favoriteService.deleteFavorite(id, loginMember);
    return ResponseEntity.noContent().build();
  }
}
