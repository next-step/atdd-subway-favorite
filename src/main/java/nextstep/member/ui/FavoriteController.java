package nextstep.member.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/favorites")
	public ResponseEntity<Void> addFavorites(@AuthenticationPrincipal LoginMember loginMember,
											 @RequestBody FavoriteRequest request) {
		FavoriteResponse favoriteResponse = favoriteService.save(loginMember.getId(), request);
		return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favoriteResponses = favoriteService.findByMemberId(loginMember.getId());
		return ResponseEntity.ok(favoriteResponses);
	}

	@DeleteMapping("/favorites/{id}")
	public ResponseEntity<Void> deleteFavorites(@PathVariable long id, @AuthenticationPrincipal LoginMember loginMember) {
		favoriteService.removeById(id, loginMember.getId());
		return ResponseEntity.noContent().build();
	}
}
