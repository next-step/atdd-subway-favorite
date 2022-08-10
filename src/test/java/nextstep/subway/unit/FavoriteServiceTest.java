package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.exception.FavoriteException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberDetails;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    FavoriteRequest request;
    Member member;
    MemberDetails memberDetails;
    Member anotherMember;
    MemberDetails anotherMemberDetails;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    private Station magok;
    private Station balsan;
    private Station woojangsan;

    @BeforeEach
    void setUp() {
        magok = stationRepository.save(new Station("magok"));
        balsan = stationRepository.save(new Station("balsan"));
        woojangsan = stationRepository.save(new Station("woojangsan"));
        request = new FavoriteRequest(magok.getId(), balsan.getId());

        member = memberRepository.save(new Member("user@email.com", "password", 22));
        memberDetails = new MemberDetails(member.getEmail(), member.getPassword(), List.of("ROLE_MEMBER"));

        anotherMember = memberRepository.save(new Member("another@email.com", "password", 30));
        anotherMemberDetails = new MemberDetails(anotherMember.getEmail(), anotherMember.getPassword(), List.of("ROLE_MEMBER"));


    }

    @DisplayName("즐겨찾기 등록")
    @Test
    void registFavorite() {

        //when
        FavoriteResponse response = favoriteService.createFavorite(memberDetails, request);

        //then
        assertThat(response.getSource()).isEqualTo(magok);
        assertThat(response.getTarget()).isEqualTo(balsan);

    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {
        //given
        favoriteService.createFavorite(memberDetails, request);

        //when
        List<FavoriteResponse> response = favoriteService.getFavorites(memberDetails);

        //then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getSource()).isEqualTo(magok);


    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        //given
        FavoriteResponse favorite = favoriteService.createFavorite(memberDetails, request);

        //when
        favoriteService.deleteFavorite(memberDetails, favorite.getId());

        //then
        assertThat(favoriteService.getFavorites(memberDetails)).hasSize(0);
    }


    @DisplayName("즐겨찾기 등록하지 않은 상태로 즐겨찾기 조회")
    @Test
    void canNotGetFavoriteWithNotRegistMember() {
        //when then
        assertThatThrownBy(() -> favoriteService.getFavorite(memberDetails, request))
                .isInstanceOf(FavoriteException.class);
    }

    @DisplayName("다른사람이 등록한 즐겨찾기 조회")
    @Test
    void canNotGetOtherFavorite() {
        //given
        favoriteService.createFavorite(memberDetails, request);

        //when then
        assertThatThrownBy(() -> favoriteService.getFavorite(anotherMemberDetails, request))
                .isInstanceOf(FavoriteException.class);
    }

    @DisplayName("본인이 등록하지 않은 역에 대한 정보로 조회하기")
    @Test
    void canNotgetFavoriteWithIllegalStationInfo() {
        //given
        favoriteService.createFavorite(memberDetails, request);
        FavoriteRequest otherRequest = new FavoriteRequest(balsan.getId(), woojangsan.getId());

        //when then
        assertThatThrownBy(() -> favoriteService.getFavorite(memberDetails, otherRequest))
                .isInstanceOf(FavoriteException.class);
    }


}
