package nextstep.line.controller;

import nextstep.line.dto.CreateLineRequest;
import nextstep.line.dto.LineResponse;
import nextstep.line.dto.LinesResponse;
import nextstep.line.dto.ModifyLineRequest;
import nextstep.line.service.LineService;
import nextstep.section.dto.SectionRequest;
import nextstep.section.dto.SectionResponse;
import nextstep.section.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;
    private SectionService sectionService;

    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest createLineRequest) {
        LineResponse line = lineService.saveLine(createLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<LinesResponse> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(
            @PathVariable Long id,
            @RequestBody ModifyLineRequest modifyLineRequest) {
        lineService.modifyLine(id, modifyLineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(
            @PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long id, @Valid @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = sectionService.createSection(id, sectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + sectionResponse.getLineId() + "/sections")).body(sectionResponse);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        sectionService.deleteSection(id, stationId);

        return ResponseEntity.ok().build();
    }
}

