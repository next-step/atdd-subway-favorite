package nextstep.subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;

@RestController
public class FavoriteController {

	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/favorite")
	@Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
	public ResponseEntity<Void> registerFavorite(
		@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest) {
		long favoriteId = favoriteService.registerFavorite(loginMember.getEmail(), favoriteRequest.getSource(),
			favoriteRequest.getTarget());
		return ResponseEntity.created(URI.create("/favorite/" + favoriteId)).build();
	}

	@DeleteMapping("/favorite/{favoriteId}")
	@Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
	public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@PathVariable Long favoriteId) {
		favoriteService.deleteFavorite(loginMember.getEmail(), favoriteId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/favorite")
	@Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok().body(favoriteService.getFavorites(loginMember.getEmail()));
	}

}
