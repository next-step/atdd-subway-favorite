package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    @Mock
    private FavoriteService favoriteService;

    private Station 강남역;
    private Station 역삼역;
    private Member member;

    @BeforeEach
    void setUp() {

        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);

        member = new Member("test@gmail.com", "1234", 20);
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    @Test
    void getFavorite() {
        Favorite favorite = new Favorite(강남역, 역삼역, member.getId());
        FavoriteResponse response = FavoriteResponse.of(favorite);
        List<FavoriteResponse> responses = Arrays.asList(response);

        when(favoriteService.findFavoritesByMemberId(member.getId())).thenReturn(responses);
        List<FavoriteResponse> getResponse = favoriteService.findFavoritesByMemberId(member.getId());

        assertThat(getResponse.get(0).getSource().getId()).isEqualTo(강남역.getId());
        assertThat(getResponse.get(0).getTarget().getId()).isEqualTo(역삼역.getId());

    }
}
