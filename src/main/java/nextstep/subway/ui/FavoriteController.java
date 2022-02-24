package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.slf4j.LoggerFactory;
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
    LoggerFactory.getLogger(this.getClass()).info("인증: {}, 바디: {} {}", loginMember.getEmail(), favoriteRequest.getSource(), favoriteRequest.getTarget());
    Long id = favoriteService.saveFavorite(loginMember.getId(), favoriteRequest);
    LoggerFactory.getLogger(this.getClass()).info("결과: {}", id);

    return ResponseEntity.created(URI.create("/favorites/" + id)).build();
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
