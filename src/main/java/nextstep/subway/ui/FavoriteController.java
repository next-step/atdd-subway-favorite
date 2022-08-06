package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.PostFavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@PostMapping
	@Secured(value = {RoleType.ROLE_ADMIN, RoleType.ROLE_MEMBER})
	public ResponseEntity<Void> save(@AuthenticationPrincipal LoginMember loginMember, @RequestBody PostFavoriteRequest request) {
		Long id = favoriteService.save(loginMember.getEmail(), request);
		return ResponseEntity.created(URI.create("/favorite/" + id)).build();
	}

	@GetMapping
	@Secured(value = {RoleType.ROLE_ADMIN, RoleType.ROLE_MEMBER})
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok(favoriteService.getFavorites(loginMember.getEmail()));
	}

	@DeleteMapping("/{id}")
	@Secured(value = {RoleType.ROLE_ADMIN, RoleType.ROLE_MEMBER})
	public ResponseEntity<Void> deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal LoginMember loginMember) {
		favoriteService.deleteFavorite(id, loginMember.getEmail());
		return ResponseEntity.noContent().build();
	}
}
