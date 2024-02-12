package nextstep.api.subway.interfaces.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.dto.inport.SectionCreateCommand;
import nextstep.api.subway.domain.dto.outport.SectionInfo;
import nextstep.api.subway.domain.service.SectionService;
import nextstep.api.subway.interfaces.dto.request.SectionCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class SectionController {

	private final SectionService sectionService;

	@PostMapping("/{lineId}/sections")
	public ResponseEntity<SectionInfo> createNewSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest createRequest) {
		SectionInfo sectionInfo = sectionService.addSection(lineId, SectionCreateCommand.from(createRequest));
		return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + sectionInfo.getId())).body(sectionInfo);
	}

	@DeleteMapping("/{lineId}/sections/{stationId}")
	public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
		sectionService.deleteSection(lineId, stationId);
		return ResponseEntity.noContent().build();
	}
}
