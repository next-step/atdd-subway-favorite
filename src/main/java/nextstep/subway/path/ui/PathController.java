package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PathController {
  private final PathService pathService;

  @GetMapping("/paths")
  public ResponseEntity<PathResponse> findPath(@ModelAttribute PathRequest request) {
    Path path = pathService.findPath(request);
    return ResponseEntity.ok(PathResponse.from(path));
  }
}
