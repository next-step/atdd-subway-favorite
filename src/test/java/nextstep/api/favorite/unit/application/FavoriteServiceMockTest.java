package nextstep.api.favorite.unit.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import nextstep.api.SubwayException;
import nextstep.api.favorite.application.FavoriteService;
import nextstep.api.favorite.application.dto.FavoriteRequest;
import nextstep.api.favorite.domain.Favorite;
import nextstep.api.favorite.domain.FavoriteRepository;
import nextstep.api.member.domain.Member;
import nextstep.api.member.domain.MemberRepository;
import nextstep.api.member.domain.exception.NoSuchMemberException;
import nextstep.api.subway.applicaion.path.PathService;
import nextstep.api.subway.domain.station.Station;
import nextstep.api.subway.domain.station.StationRepository;
import nextstep.api.subway.domain.station.exception.NoSuchStationException;
import nextstep.api.subway.unit.StationFixture;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {
    @Mock
    private PathService pathService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private final Member member = Member.basic("email@gmail.com", "password", 20);
    private final Station source = StationFixture.역삼역;
    private final Station target = StationFixture.선릉역;
    private final String email = member.getEmail();
    private final Long sourceId = source.getId();
    private final Long targetId = target.getId();

    @DisplayName("즐겨찾기를 추가한다")
    @Nested
    class CreateFavorite {
        private final FavoriteRequest request = new FavoriteRequest(sourceId, targetId);

        @Test
        void success() {
            final var favorite = makeFavorite(1L, member, source, target);

            // given
            given(memberRepository.getByEmail(email)).willReturn(member);
            given(stationRepository.getById(source.getId())).willReturn(source);
            given(stationRepository.getById(target.getId())).willReturn(target);

            doNothing().when(pathService).validateConnected(sourceId, targetId);
            when(favoriteRepository.save(any())).thenReturn(favorite);

            // when
            favoriteService.saveFavorite(email, request);

            // then
            verify(favoriteRepository).save(any());
        }

        @Nested
        class Fail {

            @Test
            void 회원이_존재하지_않는_경우() {
                // given
                given(memberRepository.getByEmail(any())).willThrow(new NoSuchMemberException(""));

                // when
                assertThatThrownBy(() -> favoriteService.saveFavorite(email, request))
                        .isInstanceOf(NoSuchMemberException.class);
            }

            @Test
            void 상행역이_존재하지_않는_경우() {
                // given
                given(memberRepository.getByEmail(email)).willReturn(member);
                given(stationRepository.getById(sourceId)).willThrow(new NoSuchStationException(""));

                // when
                assertThatThrownBy(() -> favoriteService.saveFavorite(email, request))
                        .isInstanceOf(NoSuchStationException.class);
            }

            @Test
            void 하행역이_존재하지_않는_경우() {
                // given
                given(memberRepository.getByEmail(email)).willReturn(member);
                given(stationRepository.getById(sourceId)).willReturn(source);
                given(stationRepository.getById(targetId)).willThrow(new NoSuchStationException(""));

                // when
                assertThatThrownBy(() -> favoriteService.saveFavorite(email, request))
                        .isInstanceOf(NoSuchStationException.class);
            }

            @Test
            void 상행역과_하행역이_연결되지_않은_경우() {
                // given
                given(memberRepository.getByEmail(email)).willReturn(Member.basic(email, "", 20));
                given(stationRepository.getById(sourceId)).willReturn(StationFixture.역삼역);
                given(stationRepository.getById(targetId)).willReturn(StationFixture.선릉역);

                doThrow(SubwayException.class).when(pathService).validateConnected(sourceId, targetId);

                // when
                assertThatThrownBy(() -> favoriteService.saveFavorite(email, request))
                        .isInstanceOf(SubwayException.class);
            }
        }
    }

    @DisplayName("즐겨찾기를 조회한다")
    @Nested
    class FindFavorites {

        @Test
        void success() {
            final var favorites = List.of(new Favorite(member, source, target));

            // given
            given(memberRepository.getByEmail(email)).willReturn(member);
            given(favoriteRepository.findAllByMember(member)).willReturn(favorites);

            // when
            final var actual = favoriteService.findAllFavorites(email);

            // then
            assertThat(actual).hasSize(favorites.size());
        }

        @Nested
        class Fail {

            @Test
            void 회원이_존재하지_않는_경우() {
                // given
                given(memberRepository.getByEmail(any())).willThrow(new NoSuchMemberException(""));

                // when
                assertThatThrownBy(() -> favoriteService.findAllFavorites(email))
                        .isInstanceOf(NoSuchMemberException.class);
            }
        }
    }

    @DisplayName("즐겨찾기를 삭제한다")
    @Nested
    class DeleteFavorite {

        @Test
        void success() {
            final var favoriteId = 1L;

            // given
            given(memberRepository.getByEmail(email)).willReturn(member);

            // when
            favoriteService.deleteFavorite(email, favoriteId);

            // then
            verify(favoriteRepository).deleteByIdAndMember(favoriteId, member);
        }

        @Nested
        class fail {

            @Test
            void 회원이_존재하지_않는_경우() {
                // given
                given(memberRepository.getByEmail(email)).willThrow(new NoSuchMemberException(""));

                // when
                assertThatThrownBy(() -> favoriteService.deleteFavorite(email, 1L))
                        .isInstanceOf(NoSuchMemberException.class);
            }
        }
    }

    private Favorite makeFavorite(final Long id, final Member member, final Station source, final Station target) {
        final var favorite = new Favorite(member, source, target);
        ReflectionTestUtils.setField(favorite, "id", id);
        return favorite;
    }
}