package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.exception.FavoriteOwnerException;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

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

    public void deleteFavorite(Long id) {
       favoriteProducer.deleteFavorite(id);
    }


}
