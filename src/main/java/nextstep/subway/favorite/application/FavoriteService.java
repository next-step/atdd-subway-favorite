package nextstep.subway.favorite.application;

import com.google.common.collect.Lists;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSourceId());
        Station target = stationService.findById(favoriteRequest.getTargetId());
        Favorite favorite = favoriteRepository.save(new Favorite(loginMember.getId(), source.getId(), target.getId()));
        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {

        // TODO 로그인한 계정에 해당하는 즐겨찾기 목록 가져오기
        // TODO 찾아온 목록이 로그인 계정의 즐찾인지 벨리데이션 - checkFavoriteOfMine
        return (List<FavoriteResponse>) Lists.newArrayList(
                new FavoriteResponse(1L, new StationResponse(), new StationResponse())
        );
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.getOne(favoriteId);
        // TODO 로그인한 계정의 즐찾인지 벨리데이션 추가 - checkFavoriteOfMine
        favoriteRepository.delete(favorite);
    }

    public void checkFavoriteOfMine(long l, long l1) {
    }
}
