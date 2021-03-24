package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.IsExistFavoriteException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Favorites 단위 테스트")
@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FavoriteService favoriteService;

    private StationResponse source;
    private StationResponse target;
    private FavoriteRequest favoriteRequest;

    private final long MEMBER_ID = 1L;

    @BeforeEach
    void setup() {
        source = stationService.saveStation(new StationRequest("source"));
        target = stationService.saveStation(new StationRequest("target"));
        favoriteRequest = new FavoriteRequest(
            source.getId(),
            target.getId()
        );
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    public void 즐겨찾기_추가_테스트() {
        // when
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // then
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getTarget()).isEqualTo(target);
        assertThat(favoriteResponse.getSource()).isEqualTo(source);
    }

    @DisplayName("즐겨찾기를 중복으로 추가할 경우 Exception이 발생한.")
    @Test
    public void 즐겨찾기_중복_추가_테스트() {
        // given
        favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // when - then
        assertThatExceptionOfType(IsExistFavoriteException.class)
            .isThrownBy(() -> favoriteService.addFavorite(MEMBER_ID, favoriteRequest));
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    public void 즐겨찾기_목록_조회_테스트() {
        // given
        StationResponse target2 = stationService.saveStation(new StationRequest("target2"));
        FavoriteResponse response1 = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);
        FavoriteResponse response2 = favoriteService.addFavorite(MEMBER_ID, new FavoriteRequest(
            source.getId(),
            target2.getId()
        ));

        // when
        List<FavoriteResponse> favorites = favoriteService.findAllOfMember(MEMBER_ID);

        System.out.println(favorites.size());

        // then
        assertThat(favorites)
            .containsAll(Arrays.asList(
                FavoriteResponse.of(response1.getId(), source, target),
                FavoriteResponse.of(response2.getId(), source, target2)
            ));
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    public void 즐겨찾기_삭제_테스트() {
        // given
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // when
        favoriteService.removeFavorite(favoriteResponse.getId());
        List<FavoriteResponse> favorites = favoriteService.findAllOfMember(MEMBER_ID);

        // then
        assertThat(favorites).isEmpty();

    }
}
