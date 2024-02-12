package nextstep.api.subway.interfaces.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.dto.inport.LineCreateCommand;
import nextstep.api.subway.domain.dto.inport.LineUpdateCommand;
import nextstep.api.subway.domain.service.LineService;
import nextstep.api.subway.interfaces.dto.request.LineCreateRequest;
import nextstep.api.subway.interfaces.dto.request.LineUpdateRequest;
import nextstep.api.subway.interfaces.dto.response.LineResponse;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@RestController
@RequiredArgsConstructor
public class LineController {

	private final LineService service;

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest createRequest) {
		LineResponse station = service.saveLine(LineCreateCommand.from(createRequest));
		return ResponseEntity.created(URI.create("/lines/" + station.getId())).body(station);
	}

	@GetMapping("/lines")
	public ResponseEntity<List<LineResponse>> showLines() {
		return ResponseEntity.ok().body(service.findAllLines());
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findLineById(id));
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest updateRequest) {
		return ResponseEntity.ok().body(service.updateLineById(id, LineUpdateCommand.from(updateRequest)));
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
		service.deleteLineById(id);
		return ResponseEntity.noContent().build();
	}

}
