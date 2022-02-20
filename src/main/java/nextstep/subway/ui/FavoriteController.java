package nextstep.subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.UserDetails;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<?> createFavorites(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favorite = favoriteService.createFavorites(userDetails, favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable("id") Long id) {
		favoriteService.deleteFavorite(userDetails, id);
		return ResponseEntity.notFound().build();
	}
}
