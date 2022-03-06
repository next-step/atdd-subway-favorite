package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/favorites")
	public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
		FavoriteResponse favorite = favoriteService.addFavorite(loginMember.getId(), request);

		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favorites = favoriteService.findFavorites();

		return ResponseEntity.ok().body(favorites);
	}
}
