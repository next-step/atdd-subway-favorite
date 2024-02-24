package nextstep.core.favorite.acceptance;

import nextstep.common.annotation.AcceptanceTest;
import nextstep.core.station.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.core.favorite.fixture.FavoriteFixture.추가할_즐겨찾기_정보;
import static nextstep.core.favorite.fixture.FavoriteFixture.확인할_즐겨찾기_정보;
import static nextstep.core.favorite.step.FavoriteSteps.*;
import static nextstep.core.line.fixture.LineFixture.*;
import static nextstep.core.line.step.LineSteps.지하철_노선_생성;
import static nextstep.core.member.step.AuthSteps.회원생성_후_토큰_발급;
import static nextstep.core.section.fixture.SectionFixture.지하철_구간;
import static nextstep.core.section.step.SectionSteps.성공하는_지하철_구간_추가요청;
import static nextstep.core.station.step.StationSteps.지하철_역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 인수 테스트")
@AcceptanceTest
public class FavoriteAcceptanceTest {

    final String 비정상적인_회원의_토큰 = "Bearer InValid Token";

    String 정상적인_회원의_토큰;

    Long 교대역;
    Long 강남역;
    Long 양재역;
    Long 남부터미널역;
    Long 정왕역;
    Long 오이도역;

    Long 존재하지_않는_역 = 999L;

    Long 이호선;
    Long 신분당선;
    Long 삼호선;
    Long 사호선;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     * <p>
     * 오이도역 --- *4호선* --- 정왕역
     */
    @BeforeEach
    public void 사전_노선_설정() {
        교대역 = 지하철_역_생성(StationFixture.교대역);
        강남역 = 지하철_역_생성(StationFixture.강남역);
        양재역 = 지하철_역_생성(StationFixture.양재역);
        남부터미널역 = 지하철_역_생성(StationFixture.남부터미널역);
        정왕역 = 지하철_역_생성(StationFixture.정왕역);
        오이도역 = 지하철_역_생성(StationFixture.오이도역);

        이호선 = 지하철_노선_생성(이호선(교대역, 강남역, 10));
        신분당선 = 지하철_노선_생성(신분당선(강남역, 양재역, 10));
        삼호선 = 지하철_노선_생성(삼호선(교대역, 남부터미널역, 2));
        사호선 = 지하철_노선_생성(사호선(정왕역, 오이도역, 10));

        성공하는_지하철_구간_추가요청(삼호선, 지하철_구간(남부터미널역, 양재역, 3));
    }

    @BeforeEach
    void 사전_회원의_토큰_발급() {
        var 정상적인_회원의_이메일 = "admin@email.com";
        var 정상적인_회원의_비밀번호 = "password";
        정상적인_회원의_토큰 = 회원생성_후_토큰_발급(정상적인_회원의_이메일, 정상적인_회원의_비밀번호);
    }

    @Nested
    class 즐겨찾기_추가 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  즐겨찾기 추가 요청을 할 때
             * When     회원 정보로 발급한 토큰을 같이 전달할 경우
             * Then  해당 회원의 즐겨찾기 목록에 추가된다.
             */
            @Test
            void 토큰과_함께_즐겨찾기_추가() {
                // when
                성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // then
                특정_회원의_즐겨찾기_목록_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
            }
        }

        @Nested
        class 실패 {
            @Nested
            class 회원_관련 {
                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기 추가 요청을 할 때
                 * When     회원 정보로 발급한 토큰을 같이 전달하지 않은 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 토큰없이_즐겨찾기_추가() {
                    토큰없이_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역));
                }

                /**
                 * When  즐겨찾기 추가 요청을 할 때
                 * When     회원 정보로 발급한 토큰이 아닌, 임의로 생성한 토큰을 전달한 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 비정상적인_토큰으로_즐겨찾기_추가() {
                    잘못된_토큰으로_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 비정상적인_회원의_토큰);
                }
            }

            @Nested
            class 경로_관련 {
                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기 추가 요청을 할 때
                 * When     존재하지 않는 출발역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 존재하지_않는_출발역으로_즐겨찾기_추가() {
                    // when
                    실패하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(존재하지_않는_역, 강남역), 정상적인_회원의_토큰);

                    // then
                    특정_회원의_즐겨찾기_목록_없음_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기 추가 요청을 할 때
                 * When     존재하지 않는 도착역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 존재하지_않는_도착역으로_즐겨찾기_추가() {
                    // when
                    실패하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(강남역, 존재하지_않는_역), 정상적인_회원의_토큰);

                    // then
                    특정_회원의_즐겨찾기_목록_없음_검증(확인할_즐겨찾기_정보(교대역, 존재하지_않는_역), 정상적인_회원의_토큰);
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기 추가 요청을 할 때
                 * When     출발역과 도착역이 동일할 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 출발역과_도착역이_동일하게_즐겨찾기_추가() {
                    // when
                    실패하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(강남역, 강남역), 정상적인_회원의_토큰);

                    // then
                    특정_회원의_즐겨찾기_목록_없음_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기 추가 요청을 할 때
                 * When     출발역과 도착역이 연결되지 않았을 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 출발역과_도착역이_연결되지_않은_즐겨찾기_추가() {
                    // when
                    실패하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(강남역, 오이도역), 정상적인_회원의_토큰);

                    // then
                    특정_회원의_즐겨찾기_목록_없음_검증(확인할_즐겨찾기_정보(강남역, 오이도역), 정상적인_회원의_토큰);
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
                // when
                성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // then
                특정_회원의_즐겨찾기_목록_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청할 때
             * When     회원 정보를 전달하지 않은 경우
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 없다.
             */
            @Test
            void 회원정보를_전달하지_않은_즐겨찾기_조회() {
                // given
                성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // when, then
                토큰없이_즐겨찾기_목록_검증(추가할_즐겨찾기_정보(교대역, 강남역));
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청할 때
             * When     존재하지 않는 회원 정보를 전달한 경우
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 없다.
             */
            @Test
            void 존재하지_않는_회원정보로_즐겨찾기_조회() {
                // when
                성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // then
                잘못된_토큰으로_즐겨찾기_목록_검증(추가할_즐겨찾기_정보(교대역, 강남역), 비정상적인_회원의_토큰);
            }
        }
    }

    @Nested
    class 즐겨찾기_삭제 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  생성된 회원정보로 발급된 토큰을 통해, 추가한 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 목록에서 삭제된다.
             */
            @Test
            void 존재하지_않는_회원정보로_즐겨찾기_조회() {
                // given
                var 성공하는_즐겨찾기_추가_요청 = 성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // when
                성공하는_즐겨찾기_삭제_요청(성공하는_즐겨찾기_추가_요청, 정상적인_회원의_토큰);

                // then
                특정_회원의_즐겨찾기_목록_없음_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
            }
        }

        @Nested
        class 실패 {

            String 정상적인_회원A의_토큰;
            String 정상적인_회원B의_토큰;

            @BeforeEach
            void 사전_정상적인_회원A의_토큰_발급() {
                var 정상적인_회원A의_이메일 = "admin001@email.com";
                var 정상적인_회원A의_비밀번호 = "password";
                정상적인_회원A의_토큰 = 회원생성_후_토큰_발급(정상적인_회원A의_이메일, 정상적인_회원A의_비밀번호);
            }

            @BeforeEach
            void 사전_정상적인_회원B의_토큰_발급() {
                var 정상적인_회원B의_이메일 = "admin002@email.com";
                var 정상적인_회원B의_비밀번호 = "password";
                정상적인_회원B의_토큰 = 회원생성_후_토큰_발급(정상적인_회원B의_이메일, 정상적인_회원B의_비밀번호);
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 다른 사용자의 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 다른_사용자의_즐겨찾기_삭제() {
                // given
                var 성공하는_즐겨찾기_추가_요청 = 성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원A의_토큰);

                // when
                실패하는_즐겨찾기_삭제_요청(성공하는_즐겨찾기_추가_요청, 정상적인_회원B의_토큰);

                // then
                특정_회원의_즐겨찾기_목록_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원A의_토큰);
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 존재하지 않는 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 존재하지_않는_즐겨찾기_삭제() {
                // given
                var 존재하지_않는_즐겨찾기_번호 = 999L;
                var 성공하는_즐겨찾기_추가_요청 = 성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // when
                실패하는_존재하지_않는_즐겨찾기_삭제_요청(존재하지_않는_즐겨찾기_번호, 정상적인_회원의_토큰);

                // then
                특정_회원의_즐겨찾기_목록_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 때
             * When     회원 정보를 전달하지 않은 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 회원정보_없이_즐겨찾기_삭제() {
                // given
                var 성공하는_즐겨찾기_추가_요청 = 성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // when
                토큰없이_즐겨찾기_삭제_요청(성공하는_즐겨찾기_추가_요청);

                // then
                특정_회원의_즐겨찾기_목록_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 때
             * When     존재하지 않는 회원 정보를 전달한 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 존재하지_않는_회원정보로_즐겨찾기_삭제() {
                // given
                var 성공하는_즐겨찾기_추가_요청 = 성공하는_즐겨찾기_추가_요청(추가할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);

                // when
                잘못된_토큰으로_즐겨찾기_삭제_요청(성공하는_즐겨찾기_추가_요청, 비정상적인_회원의_토큰);

                // then
                특정_회원의_즐겨찾기_목록_검증(확인할_즐겨찾기_정보(교대역, 강남역), 정상적인_회원의_토큰);
            }
        }
    }
}