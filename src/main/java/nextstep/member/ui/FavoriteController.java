package nextstep.member.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
		FavoriteResponse favorite = favoriteService.saveFavorite(loginMember.getId(), request);
		return ResponseEntity.created(URI.create("/favorites" + favorite.getId())).body(favorite);
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> showFavorite(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favorites = favoriteService.findByMemberId(loginMember.getId());
		return ResponseEntity.ok().body(favorites);
	}

	@DeleteMapping("/{favoriteId}")
	public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long favoriteId) {
		favoriteService.deleteById(loginMember.getId(), favoriteId);
		return ResponseEntity.ok().build();
	}
}
