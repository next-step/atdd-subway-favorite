package subway.favorite;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.favorite.FavoriteRequest;
import subway.dto.favorite.FavoriteResponse;
import subway.member.AuthenticationPrincipal;
import subway.member.LoginMember;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<Void> save(
		@AuthenticationPrincipal LoginMember member,
		@RequestBody FavoriteRequest request
	) {
		Long savedId = favoriteService.save(member.getEmail(), request);
		return ResponseEntity.created(URI.create("/favorites/" + savedId)).build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> favorite(@AuthenticationPrincipal LoginMember member) {
		List<FavoriteResponse> responses = favoriteService.findFavorite(member.getEmail());
		return ResponseEntity.ok().body(responses);
	}

}
