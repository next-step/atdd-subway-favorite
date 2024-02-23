package nextstep.api.favorite.interfaces;

import static org.springframework.http.ResponseEntity.*;

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
import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.api.favorite.application.facade.FavoriteFacade;
import nextstep.api.favorite.application.model.dto.FavoriteCreateRequest;
import nextstep.api.favorite.application.model.dto.FavoriteCreateResponse;
import nextstep.api.favorite.application.model.dto.FavoriteResponse;
import nextstep.common.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteFacade favoriteFacade;

	@PostMapping("/favorites")
	public ResponseEntity<FavoriteCreateResponse> create(@AuthenticationPrincipal UserPrincipal loginMember, @RequestBody FavoriteCreateRequest request) {
		FavoriteCreateResponse favoriteResponse = favoriteFacade.create(loginMember, request);
		return created(URI.create("/lines/" + favoriteResponse.getId())).body(favoriteResponse);
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserPrincipal loginMember) {
		return ok().body(favoriteFacade.findFavorites(loginMember));
	}

	@DeleteMapping("/favorites/{id}")
	public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserPrincipal loginMember, @PathVariable Long id) {
		favoriteFacade.deleteFavorite(loginMember, id);
		return noContent().build();
	}
}
