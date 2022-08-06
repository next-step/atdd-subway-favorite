package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.PostFavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
