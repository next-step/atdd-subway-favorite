package nextstep.favorite;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.dto.FavoriteRequestDto;
import nextstep.favorite.dto.FavoriteResponseDto;
import nextstep.filter.PreAuthorize;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void createFavorite(@PreAuthorize MemberResponse member, @RequestBody FavoriteRequestDto favoriteRequestDto) {
        favoriteService.create(member.getEmail(), favoriteRequestDto);
    }

    @GetMapping
    public List<FavoriteResponseDto> getListFavorite(@PreAuthorize MemberResponse member) {
        return favoriteService.getList(member.getEmail())
                .stream()
                .map(FavoriteResponseDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PreAuthorize MemberResponse member, @PathVariable Long id) {
        favoriteService.deleteById(member.getEmail(), id);
    }
}
