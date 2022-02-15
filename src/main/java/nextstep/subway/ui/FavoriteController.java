package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.util.List;

@RequestMapping("/favorites")
@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(
            @Valid @RequestBody final FavoriteRequest request,
            final BindingResult bindingResult,
            @AuthenticationPrincipal final LoginMember loginMember
    ) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException();
        }
        FavoriteResponse response = favoriteService.saveFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.findAllFavorites(loginMember.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(
            @PathVariable final Long id,
            @AuthenticationPrincipal final LoginMember loginMember
    ) {
        favoriteService.deleteFavoriteById(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
