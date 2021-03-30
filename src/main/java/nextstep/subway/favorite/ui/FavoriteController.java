package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

  private final FavoriteService favoriteService;

  public FavoriteController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  @PostMapping
  public ResponseEntity<Void> addFavorite(
      @AuthenticationPrincipal LoginMember loginMember,
      @RequestBody FavoriteRequest favoriteRequest
  ) {
    Favorite favorite = favoriteService.add(favoriteRequest, loginMember.getId());
    return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
  }

  @DeleteMapping("/{favoriteId}")
  public ResponseEntity<Void> removeFavorite(
      @AuthenticationPrincipal LoginMember loginMember,
      @PathVariable long favoriteId
  ) {
    favoriteService.remove(favoriteId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<FavoriteResponse>> findAllFavoriteOfMine(
      @AuthenticationPrincipal LoginMember loginMember
  ) {
    return ResponseEntity.ok(favoriteService.findAll(loginMember.getId()));
  }

}
