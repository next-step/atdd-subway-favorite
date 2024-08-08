package nextstep.section.ui;

import nextstep.line.application.dto.LineResponse;
import nextstep.section.application.dto.SectionRequest;
import nextstep.section.application.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {

        sectionService.saveSection(id, sectionRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long id, @RequestParam("stationId") Long stationId ) {
        LineResponse line = sectionService.deleteSection(id, stationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(line);

    }


}
