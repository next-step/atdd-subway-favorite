package nextstep.line.ui;


import nextstep.line.application.LineService;
import nextstep.line.application.dto.line.LineDto;
import nextstep.line.ui.dto.line.LineCreateRequestBody;
import nextstep.line.ui.dto.line.LineResponseBody;
import nextstep.line.ui.dto.line.LineUpdateRequestBody;
import nextstep.line.ui.dto.section.SectionCreateRequestBody;
import nextstep.line.ui.dto.section.SectionResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponseBody> createLine(@RequestBody LineCreateRequestBody lineCreateRequestBody) {
        LineDto line = lineService.saveLine(lineCreateRequestBody.toCommand());
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(LineResponseBody.create(line));
    }

    @GetMapping
    public ResponseEntity<List<LineResponseBody>> showLines() {
        List<LineDto> lines = lineService.findAllLines();
        return ResponseEntity.ok().body(LineResponseBody.create(lines));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponseBody> showLine(@PathVariable Long id) {
        LineDto line = lineService.getLineByIdOrFail(id);
        return ResponseEntity.ok().body(LineResponseBody.create(line));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable Long id,
            @RequestBody LineUpdateRequestBody lineUpdateRequestBody
    ) {
        lineService.updateLine(lineUpdateRequestBody.toCommand(id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponseBody> createLineSection(
            @PathVariable Long id,
            @RequestBody SectionCreateRequestBody sectionCreateRequestBody
    ) {
        lineService.addSection(sectionCreateRequestBody.toCommand(id));
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteLineSection(
            @PathVariable("id") Long lineId,
            @RequestParam("stationId") Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
