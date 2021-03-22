package nextstep.subway.line.application;

import nextstep.repository.MemoryFavoriteRepository;
import nextstep.repository.MemoryLineRepository;
import nextstep.repository.MemoryMemberRepository;
import nextstep.repository.MemoryStationRepository;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FavoriteService 클래스")

public class FavoriteServiceTest {
    private LineRepository lineRepository = new MemoryLineRepository();
    private StationRepository stationRepository = new MemoryStationRepository();
    private FavoriteRepository favoriteRepository = new MemoryFavoriteRepository();
    private MemberRepository memberRepository = new MemoryMemberRepository();

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private FavoriteService favoriteService;
    private Station 강남역;
    private Station 역삼역;
    private Member member;
    private Favorite favorite;

    @BeforeEach
    void setup() {
        favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);

        member = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
    }

    @Nested
    @DisplayName("saveFavorites 메서드는")
    class Describe_saveFavorites {
        @Nested
        @DisplayName("회원 아이디와 즐겨찾기 Request를 전달하면")
        class Context_with_id_and_request{
            @DisplayName("즐겨찾기를 저장하고 결과DTO를 반환한다.")
            @Test
            void it_is_return_favorite_dto(){
                //when
                FavoriteResponse response = favoriteService.saveFavorites(member.getId(), new FavoriteRequest(강남역.getId(), 역삼역.getId()));

                //then
                assertThat(response.getSource()).isEqualTo(StationResponse.of(강남역));
                assertThat(response.getTarget()).isEqualTo(StationResponse.of(역삼역));
            }
        }
    }

    @Nested
    @DisplayName("findFavoritesAllByMemberId 메서드는")
    class Describe_findFavoritesAllByMemberId{
        @Nested
        @DisplayName("회원 아이디를 전달하면")
        class Context_with_member_id{
            @DisplayName("회원의 즐겨찾기 DTO 목록을 반환한다.")
            @Test
            void it_is_list_from_favorite() {
                //given
                FavoriteResponse response = favoriteService.saveFavorites(member.getId(), new FavoriteRequest(강남역.getId(), 역삼역.getId()));

                //when
                List<FavoriteResponse> results = favoriteService.findFavoritesAllByMemberId(member.getId());

                //then
                assertThat(results).hasSize(1);
                assertThat(results.get(0)).isEqualTo(response);
            }
        }
    }

    @Nested
    @DisplayName("deleteFavorite 메서드는")
    class Describe_deleteFavorite{
        @Nested
        @DisplayName("회원아이디와 즐겨찾기 아이디를 전달하면")
        class Context_with_member_id_and_favorite_id{
            @DisplayName("해당 회원의 즐겨찾기를 삭제한다.")
            @Test
            void it_is_deleted_favorite_from_member(){
                //given
                FavoriteResponse response = favoriteService.saveFavorites(member.getId(), new FavoriteRequest(강남역.getId(), 역삼역.getId()));

                //when
                favoriteService.deleteFavorite(member.getId(), response.getId());
                List<FavoriteResponse> list = favoriteService.findFavoritesAllByMemberId(member.getId());

                //then
                assertThat(list).hasSize(0);
            }
        }
    }

}
