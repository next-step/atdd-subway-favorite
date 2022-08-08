package nextstep.subway.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteResponse;

@RestController
public class FavoriteController {

	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/favorite")
	public ResponseEntity<Void> registerFavorite(@RequestParam Long source,
		@RequestParam Long target) {
		favoriteService.registerFavorite(source, target);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/favorite/{favoriteId}")
	public ResponseEntity<Void> deleteFavorite(@PathVariable Long favoriteId) {
		favoriteService.deleteFavorite(favoriteId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/favorite")
	public ResponseEntity<List<FavoriteResponse>> getFavorites() {
		return ResponseEntity.ok().body(favoriteService.getFavorites());
	}

}
