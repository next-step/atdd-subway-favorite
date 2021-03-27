package nextstep.subway.favorite.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<Void> createFavorite(
		@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
		final Long favoriteId = favoriteService.createFavorite(member.getId(), request);
		return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
	}

	@GetMapping
	public ResponseEntity<?> getFavorites(@AuthenticationPrincipal LoginMember member) {
		return ResponseEntity.ok().build();
	}

}
