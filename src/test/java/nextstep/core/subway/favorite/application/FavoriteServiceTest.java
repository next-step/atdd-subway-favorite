package nextstep.core.subway.favorite.application;

import nextstep.common.annotation.ApplicationTest;
import nextstep.core.subway.favorite.application.FavoriteService;
import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import nextstep.core.subway.favorite.application.dto.FavoriteResponse;
import nextstep.core.subway.favorite.domain.Favorite;
import nextstep.core.subway.favorite.domain.FavoriteRepository;
import nextstep.core.subway.line.domain.Line;
import nextstep.core.subway.line.domain.LineRepository;
import nextstep.core.member.application.MemberService;
import nextstep.core.member.application.dto.MemberRequest;
import nextstep.core.member.application.dto.MemberResponse;
import nextstep.core.member.domain.Member;
import nextstep.core.member.exception.NonMatchingMemberException;
import nextstep.core.subway.pathFinder.application.PathFinderService;
import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.section.domain.SectionRepository;
import nextstep.core.subway.station.application.StationService;
import nextstep.core.subway.station.application.dto.StationResponse;
import nextstep.core.subway.station.domain.Station;
import nextstep.core.subway.station.domain.StationRepository;
import nextstep.core.subway.station.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationTest
@DisplayName("즐겨찾기 서비스 레이어 테스트")
public class FavoriteServiceTest {
    FavoriteService favoriteService;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    PathFinderService pathFinderService;

    @Autowired
    StationService stationService;


    @Autowired
    MemberService memberService;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    SectionRepository sectionRepository;

    @BeforeEach
    void 사전_서비스_객체_생성() {
        favoriteService = new FavoriteService(favoriteRepository, pathFinderService, stationService);
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
    Long 존재하지_않는_상행역_번호;
    Long 존재하지_않는_하행역_번호;

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

        sectionRepository.save(new Section(교대, 강남, 10, 이호선));
        sectionRepository.save(new Section(강남, 양재, 10, 신분당선));
        sectionRepository.save(new Section(교대, 남부터미널, 2, 삼호선));
        sectionRepository.save(new Section(남부터미널, 양재, 3, 삼호선));
        sectionRepository.save(new Section(정왕, 오이도, 3, 사호선));

        존재하지_않는_상행역_번호 = 999L;
        존재하지_않는_하행역_번호 = 998L;
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
                Member member = createMember("test@test.com", "test001!", 30);

                // when
                Favorite favorite = favoriteService.createFavorite(new FavoriteRequest(강남역_번호, 교대역_번호), member);

                // then
                assertSavedFavoriteForMember(favorite, member);
            }
        }

        @Nested
        class 실패 {
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
                    Member member = createMember("test@test.com", "test001!", 30);

                    FavoriteRequest favoriteRequest = new FavoriteRequest(존재하지_않는_상행역_번호, 교대역_번호);

                    // when, then
                    assertThatExceptionOfType(EntityNotFoundException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, member);
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
                    Member member = createMember("test@test.com", "test001!", 30);

                    FavoriteRequest favoriteRequest = new FavoriteRequest(교대역_번호, 존재하지_않는_하행역_번호);

                    // when, then
                    assertThatExceptionOfType(EntityNotFoundException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, member);
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
                    Member member = createMember("test@test.com", "test001!", 30);

                    FavoriteRequest favoriteRequest = new FavoriteRequest(교대역_번호, 교대역_번호);

                    // when, then
                    assertThatExceptionOfType(IllegalArgumentException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, member);
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
                    Member member = createMember("test@test.com", "test001!", 30);

                    FavoriteRequest favoriteRequest = new FavoriteRequest(교대역_번호, 오이도역_번호);

                    // when, then
                    assertThatExceptionOfType(IllegalArgumentException.class)
                            .isThrownBy(() -> {
                                favoriteService.createFavorite(favoriteRequest, member);
                            })
                            .withMessageMatching("출발역과 도착역을 잇는 경로가 없습니다.");
                }
            }
        }
    }

    @Nested
    class 즐겨찾기_조회 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청하면
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 있다.
             */
            @Test
            void 추가한_즐겨찾기_조회() {
                // given
                Member member = createMember("test@test.com", "test001!", 30);

                FavoriteRequest favoriteRequest = new FavoriteRequest(강남역_번호, 교대역_번호);

                // when
                Favorite savedFavorite = favoriteService.createFavorite(favoriteRequest, member);

                // then
                assertThat(favoriteService.findFavorites(member)).usingRecursiveComparison()
                        .isEqualTo(List.of(new FavoriteResponse(
                                savedFavorite.getId(),
                                new StationResponse(강남.getId(), 강남.getName()),
                                new StationResponse(교대.getId(), 교대.getName()))));
            }
        }
    }

    @Nested
    class 즐겨찾기_삭제 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 목록에서 삭제된다.
             */
            @Test
            void 즐겨찾기_추가() {
                // given
                Member member = createMember("test@test.com", "test001!", 30);

                Favorite favorite = favoriteService.createFavorite(new FavoriteRequest(강남역_번호, 교대역_번호), member);
                assertSavedFavoriteForMember(favorite, member);

                // when
                favoriteService.deleteFavorite(favorite.getId(), member);

                // then
                assertThat(favoriteRepository.findAll()).doesNotContain(favorite);
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 다른 사용자의 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 다른_사용자의_즐겨찾기_삭제() {
                // given
                Member memberA = createMember("test001@test.com", "test001!", 30);
                Member memberB = createMember("test002@test.com", "test001!", 30);

                Favorite favorite = favoriteService.createFavorite(new FavoriteRequest(강남역_번호, 교대역_번호), memberA);
                assertSavedFavoriteForMember(favorite, memberA);

                // when
                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            favoriteService.deleteFavorite(favorite.getId(), memberB);
                        })
                        .withMessageMatching("다른 회원의 즐겨찾기를 삭제할 수 없습니다.");
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 존재하지 않는 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 존재하지_않는_즐겨찾기_삭제() {
                // given
                Member member = createMember("test@test.com", "test001!", 30);

                Favorite favorite = favoriteService.createFavorite(new FavoriteRequest(강남역_번호, 교대역_번호), member);
                assertSavedFavoriteForMember(favorite, member);

                // when
                assertThatExceptionOfType(EntityNotFoundException.class)
                        .isThrownBy(() -> {
                            favoriteService.deleteFavorite(999L, member);
                        })
                        .withMessageMatching("즐겨찾기 번호에 해당하는 즐겨찾기가 없습니다.");
            }
        }
    }

    private Member createMember(String email, String password, int age) {
        Member member = new Member(email, password, age);
        MemberResponse memberResponse = memberService.createMember(new MemberRequest(email, password, age));

        ReflectionTestUtils.setField(member, "id", memberResponse.getId());
        return member;
    }

    private void assertSavedFavoriteForMember(Favorite favorite, Member member) {
        List<Favorite> favorites = favoriteRepository.findAll();

        List<Favorite> filteredByMember = favorites.stream()
                .filter(it -> it.getMember().equals(member))
                .collect(Collectors.toList());

        assertThat(favorites).isEqualTo(filteredByMember);
        assertThat(favorites).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(favorite));
    }
}
