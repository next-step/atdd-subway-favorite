package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
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
  private FavoriteService favoriteService;
  private MemberService memberService;
  public FavoriteController(FavoriteService favoriteService, MemberService memberService){
    this.favoriteService = favoriteService;
    this.memberService = memberService;
  }
  @PostMapping
  public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody FavoriteRequest favoriteRequest) {
    Long memberId = memberService.findMemberByEmail(userPrincipal.getUsername()).getId();
    FavoriteResponse favorite = favoriteService.saveFavorite(memberId, favoriteRequest);
    return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
  }

  @GetMapping
  public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    Long memberId = memberService.findMemberByEmail(userPrincipal.getUsername()).getId();
    List<FavoriteResponse> responses = favoriteService.findFavorite(memberId);
    return ResponseEntity.ok().body(responses);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id ) {
    favoriteService.deleteLine(id);
    return ResponseEntity.noContent().build();
  }

}
