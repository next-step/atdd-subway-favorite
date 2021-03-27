package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.common.exception.ErrorResponse;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.InvalidFavoriteMemberException;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavoriteResponsesByMemberId(loginMember.getId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteResponses);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<URI> addFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                           @RequestBody FavoriteRequest favoriteRequest) {
        Long id = favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
        URI createdURI = URI.create(String.format("/favorites/%d", id));

        return ResponseEntity.created(createdURI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @DeleteMapping(value = "/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @PathVariable Long favoriteId) {
        favoriteService.removeFavorite(favoriteId, loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(InvalidFavoriteMemberException.class)
    public ResponseEntity<ErrorResponse> invalidFavoriteMemberExceptionHandler(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
