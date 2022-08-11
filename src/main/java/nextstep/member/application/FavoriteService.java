package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteProducer favoriteProducer;

    public FavoriteResponse saveFavorite(String username, FavoriteRequest request) {
        return FavoriteResponse.of(favoriteProducer.saveFavorite(username, request));
    }

    public FavoriteResponse findFavorite(Long id) {
        return FavoriteResponse.of(favoriteProducer.findFavorite(id));
    }

    public List<FavoriteResponse> findAll(String username) {
        return FavoriteResponse.of(favoriteProducer.findAllByUsername(username));
    }

    public void deleteFavorite(Long id) {
       favoriteProducer.deleteFavorite(id);
    }


}
