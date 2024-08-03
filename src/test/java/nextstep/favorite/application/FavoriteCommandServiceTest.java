package nextstep.favorite.application;

import nextstep.favorite.domain.FavoriteRepository;
import nextstep.path.application.PathQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class FavoriteCommandServiceTest {

    private FavoriteRepository favoriteRepository;
    private PathQueryService pathQueryService;
    private FavoriteCommandService favoriteCommandService;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
    }

    class WhenAdd {

        //TODO 끊어진 경로인 경우 생성 불가

        @DisplayName("끊어진 경로인 경우 생성되지 않는다")
        @Test
        void test1() {

        }

        //TODO source target 같으면 생성 불가
        @DisplayName("두 경로가 같을 경우 생성되지 않는다")
        @Test
        void test2() {

        }

        //TODO 동일 경로가 있는경우 생성 불가

        //TODO 로그인 유저가 아닌 경우 생성 불가

    }




}
