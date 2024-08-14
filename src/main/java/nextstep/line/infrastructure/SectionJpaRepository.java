package nextstep.line.infrastructure;

import nextstep.line.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionJpaRepository extends JpaRepository<Section, Long>, SectionRepository {

}
