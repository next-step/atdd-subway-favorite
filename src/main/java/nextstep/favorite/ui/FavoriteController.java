package nextstep.favorite.ui;

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

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.application.dto.AuthUser;
import nextstep.member.ui.annotations.AuthToken;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoriteResponse> createFavorite(@AuthToken AuthUser authUser,
		@RequestBody FavoriteRequest request) {
		FavoriteResponse favorite = favoriteService.saveFavorite(authUser, request);
		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthToken AuthUser authUser) {
		return ResponseEntity.ok().body(favoriteService.findAllFavorites(authUser));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFavorite(@AuthToken AuthUser authUser, @PathVariable Long id) {
		favoriteService.deleteFavoriteById(authUser, id);
		return ResponseEntity.noContent().build();
	}
}
