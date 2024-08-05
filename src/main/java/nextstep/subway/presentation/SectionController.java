package nextstep.subway.presentation;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/lines/{lineId}/sections")
@RestController
public class SectionController {
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = sectionService.addSection(lineId, sectionRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + lineId + "/sections/" + sectionResponse.getSectionId()))
                .body(sectionResponse);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
