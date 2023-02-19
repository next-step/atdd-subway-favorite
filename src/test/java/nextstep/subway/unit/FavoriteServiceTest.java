package nextstep.subway.unit;

import nextstep.auth.domain.LoginUserInfo;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.acceptance.ApplicationContextTest;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.config.exception.NotFoundException;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.acceptance.MemberSteps.createMember;
import static nextstep.subway.config.message.SubwayError.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 테스트")
@Transactional
class FavoriteServiceTest extends ApplicationContextTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteService favoriteService;

    @DisplayName("로그인한 사용자는 즐겨찾기를 저장한다.")
    @Test
    void saveFavorite() {

        final Member 사용자1 = memberRepository.save(createMember(EMAIL, PASSWORD, 10));
        final Station 강남역 = stationRepository.save(new Station("강남역"));
        final Station 역삼역 = stationRepository.save(new Station("역삼역"));

        final LoginUserInfo loginUserInfo = LoginUserInfo.from(사용자1);
        final FavoriteRequest favoriteRequest = FavoriteRequest.of(강남역.getId(), 역삼역.getId());
        final FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginUserInfo, favoriteRequest);

        assertAll(
                () -> assertThat(favoriteResponse.getId()).isNotNull(),
                () -> assertThat(favoriteResponse.getSource()).isEqualTo(StationResponse.from(강남역)),
                () -> assertThat(favoriteResponse.getTarget()).isEqualTo(StationResponse.from(역삼역))
        );
    }

    @DisplayName("로그인한 사용자는 저장된 즐겨찾기를 보여준다.")
    @Test
    void showFavorite() {

        final Member 사용자1 = memberRepository.save(createMember(EMAIL, PASSWORD, 10));
        final Station 강남역 = stationRepository.save(new Station("강남역"));
        final Station 역삼역 = stationRepository.save(new Station("역삼역"));
        final LoginUserInfo loginUserInfo = LoginUserInfo.from(사용자1);
        final FavoriteRequest favoriteRequest = FavoriteRequest.of(강남역.getId(), 역삼역.getId());
        final FavoriteResponse saveFavoriteResponse = favoriteService.saveFavorite(loginUserInfo, favoriteRequest);

        final FavoriteResponse findFavoriteResponse = favoriteService.showFavorite(loginUserInfo, saveFavoriteResponse.getId());

        assertAll(
                () -> assertThat(findFavoriteResponse.getId()).isNotNull(),
                () -> assertThat(findFavoriteResponse.getSource()).isEqualTo(StationResponse.from(강남역)),
                () -> assertThat(findFavoriteResponse.getTarget()).isEqualTo(StationResponse.from(역삼역))
        );
    }

    @DisplayName("로그인한 사용자는 즐겨찾기를 제거한다.")
    @Test
    void removeFavorite() {

        final Member 사용자1 = memberRepository.save(createMember(EMAIL, PASSWORD, 10));
        final Station 강남역 = stationRepository.save(new Station("강남역"));
        final Station 역삼역 = stationRepository.save(new Station("역삼역"));
        final LoginUserInfo loginUserInfo = LoginUserInfo.from(사용자1);
        final FavoriteRequest favoriteRequest = FavoriteRequest.of(강남역.getId(), 역삼역.getId());
        final FavoriteResponse saveFavoriteResponse = favoriteService.saveFavorite(loginUserInfo, favoriteRequest);

        favoriteService.removeFavorite(loginUserInfo, saveFavoriteResponse.getId());

        assertThatThrownBy(() -> favoriteService.showFavorite(loginUserInfo, saveFavoriteResponse.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NOT_FOUND.getMessage());
    }
}
