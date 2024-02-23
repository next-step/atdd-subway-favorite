package nextstep.core.favorite.application;

import nextstep.common.annotation.ApplicationTest;
import nextstep.core.favorite.application.dto.FavoriteRequest;
import nextstep.core.favorite.domain.Favorite;
import nextstep.core.favorite.domain.FavoriteRepository;
import nextstep.core.line.domain.Line;
import nextstep.core.line.domain.LineRepository;
import nextstep.core.member.application.MemberService;
import nextstep.core.member.application.dto.MemberRequest;
import nextstep.core.member.domain.LoginMember;
import nextstep.core.member.domain.Member;
import nextstep.core.member.exception.NotFoundMemberException;
import nextstep.core.pathFinder.application.PathFinderService;
import nextstep.core.section.domain.Section;
import nextstep.core.section.domain.SectionRepository;
import nextstep.core.section.fixture.SectionFixture;
import nextstep.core.station.application.StationService;
import nextstep.core.station.domain.Station;
import nextstep.core.station.domain.StationRepository;
import nextstep.core.station.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.core.station.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationTest
@DisplayName("즐겨찾기 서비스 레이어 테스트")
public class FavoriteServiceTest {

    FavoriteService favoriteService;

    @Autowired
    MemberService memberService;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    PathFinderService pathFinderService;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    SectionRepository sectionRepository;

    @BeforeEach
    void 사전_서비스_객체_생성() {
        favoriteService = new FavoriteService(favoriteRepository, memberService, pathFinderService);
    }

    Station 교대;
    Station 강남;
    Station 양재;
    Station 남부터미널;
    Station 정왕;
    Station 오이도;
    Station 가산디지털단지;

    Long 교대역_번호;
    Long 강남역_번호;
    Long 양재역_번호;
    Long 남부터미널역_번호;
    Long 정왕역_번호;
    Long 오이도역_번호;
    Long 가산디지털단지역_번호;

    Line 이호선;
    Line 신분당선;
    Line 삼호선;
    Line 사호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     * <p>
     * <p>
     * 오이도역 --- *4호선* --- 정왕역
     */
    @BeforeEach
    void 사전_노선_설정() {
        교대 = stationRepository.save(StationFixture.교대);
        강남 = stationRepository.save(StationFixture.강남);
        양재 = stationRepository.save(StationFixture.양재);
        남부터미널 = stationRepository.save(StationFixture.남부터미널);
        정왕 = stationRepository.save(StationFixture.정왕);
        오이도 = stationRepository.save(StationFixture.오이도);
        가산디지털단지 = stationRepository.save(StationFixture.가산디지털단지);

        교대역_번호 = 교대.getId();
        강남역_번호 = 강남.getId();
        양재역_번호 = 양재.getId();
        남부터미널역_번호 = 남부터미널.getId();
        정왕역_번호 = 정왕.getId();
        오이도역_번호 = 오이도.getId();
        가산디지털단지역_번호 = 가산디지털단지.getId();

        이호선 = lineRepository.save(new Line("이호선", "green"));
        신분당선 = lineRepository.save(new Line("신분당선", "red"));
        삼호선 = lineRepository.save(new Line("삼호선", "orange"));
        사호선 = lineRepository.save(new Line("사호선", "blue"));

        Section 교대_강남_구간 = sectionRepository.save(new Section(교대, 강남, 10, 이호선));
        Section 강남_양재_구간 = sectionRepository.save(new Section(강남, 양재, 10, 신분당선));
        Section 교대_남부터미널_구간 = sectionRepository.save(new Section(교대, 남부터미널, 2, 삼호선));
        Section 남부터미널_양재_구간 = sectionRepository.save(new Section(남부터미널, 양재, 3, 삼호선));
        Section 정왕_오이도_구간 = sectionRepository.save(new Section(정왕, 오이도, 3, 사호선));
    }

    @Nested
    class 즐겨찾기_추가 {

        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  즐겨 찾기 정보와 회원 정보를 통해 즐겨 찾기를 생성한다.
             * Then  해당 회원의 즐겨찾기 목록에 추가된다.
             */
            @Test
            void 즐겨찾기_추가() {
                // given
                LoginMember loginMember = new LoginMember("test@test.com");
                Member member = new Member("test@test.com", "test001!", 30);
                memberService.createMember(new MemberRequest("test@test.com", "test001!", 30));

                FavoriteRequest favoriteRequest = new FavoriteRequest(강남역_번호, 교대역_번호);

                // when
                favoriteService.createFavorite(favoriteRequest, loginMember);

                // then
                assertThat(favoriteRepository.findAll()).usingRecursiveComparison()
                        .ignoringFields("id", "member.id")
                        .isEqualTo(List.of(new Favorite(member)));
            }
        }

        @Nested
        class 실패 {
            @Nested
            class 회원_관련 {
                /**
                 * When  즐겨 찾기 정보와 회원 정보를 통해 즐겨 찾기를 생성할 때,
                 * When    회원 정보를 찾을 수 없을 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가될 수 없다.
                 */
                @Test
                void 존재하지_않는_회원() {
                    // given
                    LoginMember loginMember = new LoginMember("test@test.com");
                    Member member = new Member("test@test.com", "test001!", 30);

                    FavoriteRequest favoriteRequest = new FavoriteRequest(강남역_번호, 교대역_번호);

                    // when
                    assertThatExceptionOfType(NotFoundMemberException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, loginMember);
                            })
                            .withMessageMatching("회원 정보가 없습니다.");
                }
            }

            @Nested
            class 경로_관련 {

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     존재하지 않는 출발역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가될 수 없다.
                 */
                @Test
                void 존재하지_않는_출발역으로_즐겨찾기_추가() {
                    // given
                    LoginMember loginMember = new LoginMember("test@test.com");
                    Member member = new Member("test@test.com", "test001!", 30);
                    memberService.createMember(new MemberRequest("test@test.com", "test001!", 30));

                    Long 존재하지_않는_상행역_번호 = 999L;
                    FavoriteRequest favoriteRequest = new FavoriteRequest(존재하지_않는_상행역_번호, 교대역_번호);

                    // when, then
                    assertThatExceptionOfType(EntityNotFoundException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, loginMember);
                            })
                            .withMessageMatching("역 번호에 해당하는 역이 없습니다.");
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     존재하지 않는 도착역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가될 수 없다.
                 */
                @Test
                void 존재하지_않는_도착역으로_즐겨찾기_추가() {
                    // given
                    LoginMember loginMember = new LoginMember("test@test.com");
                    Member member = new Member("test@test.com", "test001!", 30);
                    memberService.createMember(new MemberRequest("test@test.com", "test001!", 30));

                    Long 존재하지_않는_하행역_번호 = 999L;
                    FavoriteRequest favoriteRequest = new FavoriteRequest(교대역_번호, 존재하지_않는_하행역_번호);

                    // when, then
                    assertThatExceptionOfType(EntityNotFoundException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, loginMember);
                            })
                            .withMessageMatching("역 번호에 해당하는 역이 없습니다.");
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     출발역과 도착역이 동일할 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가될 수 없다.
                 */
                @Test
                void 출발역과_도착역이_동일하게_즐겨찾기_추가() {
                    // given
                    LoginMember loginMember = new LoginMember("test@test.com");
                    Member member = new Member("test@test.com", "test001!", 30);
                    memberService.createMember(new MemberRequest("test@test.com", "test001!", 30));

                    FavoriteRequest favoriteRequest = new FavoriteRequest(교대역_번호, 교대역_번호);

                    // when, then
                    assertThatExceptionOfType(IllegalArgumentException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, loginMember);
                            })
                            .withMessageMatching("출발역과 도착역이 동일할 수 없습니다.");
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     출발역과 도착역이 동일할 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가될 수 없다.
                 */
                @Test
                void 출발역과_도착역이_연결되지_않은_즐겨찾기_추가() {
                    // given
                    LoginMember loginMember = new LoginMember("test@test.com");
                    Member member = new Member("test@test.com", "test001!", 30);
                    memberService.createMember(new MemberRequest("test@test.com", "test001!", 30));

                    FavoriteRequest favoriteRequest = new FavoriteRequest(교대역_번호, 오이도역_번호);

                    // when, then
                    assertThatExceptionOfType(IllegalArgumentException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, loginMember);
                            })
                            .withMessageMatching("출발역과 도착역이 연결되어 있지 않습니다.");
                }
            }
        }
    }

}
