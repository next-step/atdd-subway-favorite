package nextstep.api.subway.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.service.PathService;
import nextstep.api.subway.interfaces.dto.response.PathResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/paths")
public class PathController {

	private final PathService pathService;

	@GetMapping
	public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source, @RequestParam Long target) {
		PathResponse pathResponse = pathService.findShortestPath(source, target);
		return ResponseEntity.ok(pathResponse);
	}
}
