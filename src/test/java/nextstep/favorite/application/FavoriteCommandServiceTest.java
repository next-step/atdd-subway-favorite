package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exceptions.FavoriteAlreadyExistsException;
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

        @Test
        @DisplayName("동일 경로가 이미 등록된 경우 생성되지 않는다")
        void whenPathEarlyExistsThenThrow() {
            Long memberId = 1L;
            Long source = 1L;
            Long target = 2L;

            //동일 경로가 이미 등록된 경우
            when(favoriteRepository.findByMemberIdAndSourceStationIdAndTargetStationId(memberId,source, target))
                    .thenReturn(Optional.of(new Favorite(memberId , source ,target)));

            //에러를 반환한다
            assertThrows(FavoriteAlreadyExistsException.class, () ->
                    favoriteCommandService.createFavorite(memberId, new FavoriteRequest(source, target))
            );
        }

    }

}
