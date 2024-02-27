package nextstep.subway.ui.controller;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	ResponseEntity<PathResponse> getLines(@RequestParam Long source, @RequestParam Long target) {
		if(source.equals(target)) {
			throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
		}

		return ResponseEntity.ok().body(pathService.getPath(source, target));
	}
}
