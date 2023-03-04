package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.abstractive.MemberProvider;
import nextstep.member.config.argument.annotation.MemberInfo;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteReadResponse;
import nextstep.subway.domain.Favorite;
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
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> createFavorite(@MemberInfo MemberProvider member,
                                               @RequestBody FavoriteCreateRequest request) {
        Favorite favorite = favoriteService.createFavorite(member, request);
        return ResponseEntity
                .created(URI.create("/favorites/" + favorite.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteReadResponse>> readFavorites(@MemberInfo MemberProvider member) {
        List<FavoriteReadResponse> favorites = favoriteService.readFavorites(member);
        return ResponseEntity
                .ok(favorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@MemberInfo MemberProvider memberProvider,
                                               @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(memberProvider, favoriteId);
        return ResponseEntity
                .noContent()
                .build();
    }

}
