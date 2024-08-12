package nextstep.line.infrastructure;

import java.util.List;
import nextstep.line.domain.Section;

public interface SectionRepository {
    List<Section> findAll();
}
