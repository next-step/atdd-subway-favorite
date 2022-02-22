package nextstep.member.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

  private final FavoriteService favoriteService;

  public FavoriteController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  @PostMapping
  public ResponseEntity<URI> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
    Long id = favoriteService.saveFavorite(loginMember.getId(), favoriteRequest);
    return ResponseEntity.created(URI.create("/favorite/" + id)).build();
  }

  @GetMapping
  public ResponseEntity<List<FavoriteResponse>> searchFavorite(@AuthenticationPrincipal LoginMember loginMember) {
    List<FavoriteResponse> favorites = favoriteService.searchFavorites(loginMember.getId());
    return ResponseEntity.ok().body(favorites);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
    favoriteService.deleteFavorite(id);
    return ResponseEntity.noContent().build();
  }
}
