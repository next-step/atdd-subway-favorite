package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.UserDetails;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService service;

    @PostMapping
    @Secured("ROLE_SUBSCRIPTION_MEMBER")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserDetails user, @RequestBody FavoriteRequest request) {
        FavoriteResponse response = service.saveFavorite(user.getUsername(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/{id}")
    @Secured("ROLE_SUBSCRIPTION_MEMBER")
    public ResponseEntity<FavoriteResponse> findFavorite(@PathVariable Long id) {
        FavoriteResponse response = service.findFavorite(id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_SUBSCRIPTION_MEMBER")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id) {
        service.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

}
