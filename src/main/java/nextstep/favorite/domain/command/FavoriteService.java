package nextstep.favorite.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.domain.entity.Favorite;
import nextstep.favorite.domain.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private FavoriteRepository favoriteRepository;

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     *
     * @param command
     */
    public void createFavorite(FavoriteCommand.CreateFavorite command) {
        Favorite favorite = new Favorite();
        favoriteRepository.save(favorite);
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
