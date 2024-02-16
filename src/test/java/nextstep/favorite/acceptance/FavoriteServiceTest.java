package nextstep.favorite.acceptance;

import nextstep.favorite.CannotFavoriteNonexistentPathException;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.line.LineCreateRequest;
import nextstep.line.LineResponse;
import nextstep.line.LineService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.section.SectionAddRequest;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FavoriteService favoriteService;

    Member member1;
    Member member2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("member1@gmail.com", "password", 20));
        member2 = memberRepository.save(new Member("member2@gmail.com", "password", 21));

        Station banghwa = stationRepository.save(new Station("방화역"));   // 1L;
        Station gangdong = stationRepository.save(new Station("강동역"));  // 2L;
        Station macheon = stationRepository.save(new Station("마천역"));   // 3L;
        LineResponse line5 = lineService.create(new LineCreateRequest(
                "5호선",
                "purple",
                5,
                banghwa.getId(),
                gangdong.getId()
        ));

        lineService.addSection(line5.getId(), new SectionAddRequest(
                gangdong.getId(),
                macheon.getId(),
                3
        ));

        Station gangnam = stationRepository.save(new Station("강남역"));   // 4L;
        Station samseong = stationRepository.save(new Station("삼성역"));  // 5L;
        lineService.create(new LineCreateRequest(
                "2호선",
                "green",
                4,
                gangnam.getId(),
                samseong.getId()
        ));
    }

    @Test
    void cannotFavoriteNonexistentPath() {
        // given
        Long banghwaId = 1L;
        Long gangnamId = 4L;

        // when & then
        assertThrows(CannotFavoriteNonexistentPathException.class, () -> {
            favoriteService.createFavorite(new FavoriteRequest(banghwaId, gangnamId), member1.getId());
        });
    }
}
