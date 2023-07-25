package nextstep.subway.section.presentation;

import nextstep.subway.line.application.LineService;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lines/{lineId}")
@RestController
public class SectionController {
    private final LineService lineService;

    public SectionController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/sections")
    public ResponseEntity<SectionResponse> registerSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        return ResponseEntity.ok().body(lineService.registerSection(lineId, sectionRequest));
    }

    @DeleteMapping("/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
