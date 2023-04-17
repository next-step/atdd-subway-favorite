package nextstep.favorite;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.dto.FavoriteRequestDto;
import nextstep.favorite.dto.FavoriteResponseDto;
import nextstep.filter.PreAuthorize;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createFavorite(@PreAuthorize MemberResponse member, @RequestBody FavoriteRequestDto favoriteRequestDto) {
        Favorite favorite = favoriteService.create(member.getEmail(), favoriteRequestDto);
        return ResponseEntity
                .created(URI.create("/favorites/" + favorite.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponseDto>> getListFavorite(@PreAuthorize MemberResponse member) {
        List<FavoriteResponseDto> favorites = favoriteService.getList(member.getEmail())
                .stream()
                .map(FavoriteResponseDto::of)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteFavorite(@PreAuthorize MemberResponse member, @PathVariable Long id) {
        favoriteService.deleteById(member.getEmail(), id);
        return ResponseEntity.noContent().build();
    }
}
