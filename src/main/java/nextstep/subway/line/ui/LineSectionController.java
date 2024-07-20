package nextstep.subway.line.ui;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.LineSectionService;
import nextstep.subway.line.application.dto.AppendLineSectionRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LineSectionController {
  private final LineSectionService lineSectionService;

  @PostMapping("/lines/{lineId}/sections")
  public ResponseEntity<LineResponse> appendLineSection(
      @PathVariable Long lineId, @RequestBody AppendLineSectionRequest request) {
    Line line = lineSectionService.appendLineSection(lineId, request);
    return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections"))
        .body(LineResponse.from(line));
  }

  @DeleteMapping("/lines/{lineId}/sections")
  public ResponseEntity<Void> removeLineSection(
      @PathVariable Long lineId, @RequestParam Long stationId) {
    lineSectionService.removeLineSection(lineId, stationId);
    return ResponseEntity.noContent().build();
  }
}
