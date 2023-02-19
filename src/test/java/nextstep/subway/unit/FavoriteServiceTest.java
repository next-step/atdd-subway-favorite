package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Member member;
    private Station 수서역;
    private Station 복정역;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("member1@gmail.com", "pass1234", 20, List.of(RoleType.ROLE_MEMBER.name())));

        수서역 = stationRepository.save(new Station("수서역"));
        복정역 = stationRepository.save(new Station("복정역"));
    }

    @DisplayName("즐겨찾기 구간을 생성한다.")
    @Test
    void create() {
        // given
        FavoriteRequest request = new FavoriteRequest(수서역.getId(), 복정역.getId());

        // when
        FavoriteResponse response = favoriteService.createFavorite(member.getId(), request);

        // then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(1L),
            () -> assertThat(response.getSource().getId()).isEqualTo(수서역.getId()),
            () -> assertThat(response.getTarget().getId()).isEqualTo(복정역.getId())
        );
    }
}
