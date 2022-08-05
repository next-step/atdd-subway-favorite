package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.member.domain.User;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

  private final FavoriteService favoriteService;

  public FavoriteController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  @PostMapping("/favorites")
  @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
  public ResponseEntity<FavoriteResponse> saveFavorite(@RequestBody FavoriteRequest favoriteRequest, @AuthenticationPrincipal User user) {
    FavoriteResponse favoriteResponse = favoriteService.saveFavorite(favoriteRequest, user);
    return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
  }

  @GetMapping("/favorites")
  @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
  public ResponseEntity<List<FavoriteResponse>> getFavorite(@AuthenticationPrincipal User user) {
    List<FavoriteResponse> favorites = favoriteService.getFavorite(user);
    return ResponseEntity.ok().body(favorites);
  }

  @DeleteMapping("/favorites/{favoriteId}")
  @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
  public ResponseEntity<Void> deleteFavorite(@PathVariable Long favoriteId) {
    favoriteService.deleteFavorite(favoriteId);
    return ResponseEntity.noContent().build();
  }
}
