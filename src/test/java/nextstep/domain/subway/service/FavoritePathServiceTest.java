package nextstep.domain.subway.service;

import nextstep.auth.authentication.LoginMember;
import nextstep.domain.member.domain.LoginMemberImpl;
import nextstep.domain.subway.domain.FavoritePath;
import nextstep.domain.subway.domain.FavoritePathRepository;
import nextstep.domain.subway.domain.Station;
import nextstep.domain.subway.domain.StationRepository;
import nextstep.domain.subway.dto.FavoritePathRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritePathServiceTest {

    private FavoritePathService favoritePathService;

    @Mock
    private FavoritePathRepository favoritePathRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        favoritePathService = new FavoritePathService(favoritePathRepository, stationRepository);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        //given
        FavoritePathRequest favoritePathRequest = new FavoritePathRequest(1L, 2L);
        FavoritePath favoritePath = getFavoritePath();
        LoginMember loginMember = getLoginMember();
        when(stationRepository.findById(1L)).thenReturn(Optional.of(new Station("AStation")));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(new Station("BStation")));
        when(favoritePathRepository.save(any())).thenReturn(favoritePath);

        //when
        int favoriteId = favoritePathService.createFavorite(loginMember, favoritePathRequest);

        //then
        assertThat(favoriteId).isEqualTo(favoritePath.getId());
    }

    private LoginMember getLoginMember() {
        LoginMember loginMember = new LoginMemberImpl("email@email.com","password", 20);
        ReflectionTestUtils.setField(loginMember, "id", 1L);
        return loginMember;
    }

    private FavoritePath getFavoritePath() {
        FavoritePath favoritePath = new FavoritePath();
        ReflectionTestUtils.setField(favoritePath, "id", 1L);
        return favoritePath;
    }
}