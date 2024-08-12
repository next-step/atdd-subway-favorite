package nextstep.unit.fixture;

import java.util.List;
import nextstep.line.domain.Section;
import nextstep.line.infrastructure.SectionRepository;

@SuppressWarnings("NonAsciiCharacters")
public class SectionInMemoryJpaRepository extends PathFixture implements SectionRepository {

    public SectionInMemoryJpaRepository() {
        setUp();
    }

    @Override
    public List<Section> findAll() {
        return List.of(교대역_강남역_구간,
                강남역_양재역_구간,
                교대역_남부터미널_구간,
                남부터미널_양재역_구간);
    }
}
