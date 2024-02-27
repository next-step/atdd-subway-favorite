package nextstep.subway.application.service;

import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {
	private final SectionRepository sectionRepository;

	public SectionService(SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}

	public List<Section> findAll() {
		return sectionRepository.findAll();
	}
}
