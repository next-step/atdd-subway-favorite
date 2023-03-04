package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.member.fake.ui.GithubResponses.사용자3;
import static nextstep.utils.DataLoader.MEMBER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    private Member 본인;
    private Member 타인;
    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        본인 = memberRepository.findByEmail(MEMBER_EMAIL).get();
        타인 = memberRepository.findByEmail(사용자3.getEmail()).get();

        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void saveFavorite() {
        // When
        FavoriteResponse response = favoriteService.saveFavorite(본인.getEmail(), 강남역.getId(), 역삼역.getId());

        // Then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getDepartureStationResponse()).isEqualTo(StationResponse.from(강남역)),
                () -> assertThat(response.getDestinationStationResponse()).isEqualTo(StationResponse.from(역삼역))
        );
    }

    @DisplayName("계정에 저장된 즐겨찾기 목록을 조회한다.")
    @Test
    void findFavorites() {
        // Given
        favoriteService.saveFavorite(본인.getEmail(), 강남역.getId(), 역삼역.getId());

        // When
        List<FavoriteResponse> responses = favoriteService.findFavorites(본인.getEmail());

        // Then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(1),
                () -> assertThat(responses.get(0).getId()).isEqualTo(1),
                () -> assertThat(responses.get(0).getDepartureStationResponse()).isEqualTo(StationResponse.from(강남역)),
                () -> assertThat(responses.get(0).getDestinationStationResponse()).isEqualTo(StationResponse.from(역삼역))
        );
    }

    @DisplayName("계정에 저장된 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // Given
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(본인.getEmail(), 강남역.getId(), 역삼역.getId());

        // When
        favoriteService.deleteFavorite(본인.getEmail(), favoriteResponse.getId());

        //Then
        List<FavoriteResponse> responses = favoriteService.findFavorites(본인.getEmail());
        assertThat(responses.size()).isEqualTo(0);
    }
}
