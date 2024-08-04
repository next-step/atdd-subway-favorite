package nextstep.favorite.application;

import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.payload.FavoriteRequest;
import nextstep.path.application.PathQueryService;
import nextstep.path.exceptions.PathNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FavoriteCommandServiceTest {

    private FavoriteRepository favoriteRepository;
    private PathQueryService pathQueryService;
    private FavoriteCommandService favoriteCommandService;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        pathQueryService = mock(PathQueryService.class);
        favoriteCommandService = new FavoriteCommandService(favoriteRepository, pathQueryService);
    }

    @Nested
    class WhenAdd {

        //TODO 끊어진 경로인 경우 생성 불가
        @DisplayName("끊어진 경로인 경우 생성되지 않는다")
        @Test
        void whenBrokenPathThenThrow() {
            Long memberId = 1L;
            Long source = 1L;
            Long target = 2L;

            //When 만약 경로가 끊어진 경우
            when(pathQueryService.findShortestPath(source, target)).thenReturn(Optional.empty());

            //에러를 반환한다
            assertThrows(PathNotFoundException.class, () ->
                    favoriteCommandService.createFavorite(memberId, new FavoriteRequest(source, target))
            );
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
