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

import lombok.RequiredArgsConstructor;
import nextstep.auth.Auth;
import nextstep.auth.AuthMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@PostMapping("/favorites")
	public ResponseEntity createFavorite(@Auth AuthMember authMember, @RequestBody FavoriteRequest request) {
		Long id = favoriteService.createFavorite(authMember.getId(), request);
		return ResponseEntity.created(URI.create("/favorites/" + id)).build();
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@Auth AuthMember authMember) {
		List<FavoriteResponse> favorites = favoriteService.showFavorite(authMember.getId());
		return ResponseEntity.ok().body(favorites);
	}

	@DeleteMapping("/favorites/{id}")
	public ResponseEntity deleteFavorite(@Auth AuthMember authMember, @PathVariable Long id) {
		favoriteService.deleteFavorite(authMember.getId(), id);
		return ResponseEntity.noContent().build();
	}
}
