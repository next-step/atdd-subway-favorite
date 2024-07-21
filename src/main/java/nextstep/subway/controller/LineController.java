package nextstep.subway.controller;

import lombok.RequiredArgsConstructor;
import nextstep.subway.controller.dto.AddSectionRequest;
import nextstep.subway.domain.command.LineCommand;
import nextstep.subway.domain.command.LineCommander;
import nextstep.subway.domain.query.LineReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import nextstep.subway.controller.dto.CreateLineRequest;
import nextstep.subway.controller.dto.UpdateLineRequest;
import nextstep.subway.domain.view.LineView;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineCommander lineCommander;
    private final LineReader lineReader;

    @PostMapping()
    public ResponseEntity<LineView.Main> createLine(@RequestBody CreateLineRequest request) {
        Long id = lineCommander.createLine(request.toCommand());
        LineView.Main view = lineReader.getOneById(id);
        return ResponseEntity.created(URI.create("/lines/" + id)).body(view);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineView.Main> updateLine(
            @PathVariable Long id,
            @RequestBody UpdateLineRequest request
    ) {
        lineCommander.updateLine(request.toCommand(id));
        LineView.Main view = lineReader.getOneById(id);
        return ResponseEntity.ok().body(view);
    }

    @GetMapping()
    public ResponseEntity<List<LineView.Main>> showLines() {
        List<LineView.Main> views = lineReader.getAllLines();
        return ResponseEntity.ok().body(views);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineView.Main> showLine(@PathVariable Long id) {
        LineView.Main view = lineReader.getOneById(id);
        return ResponseEntity.ok().body(view);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineCommander.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> addSection(
            @PathVariable Long lineId,
            @RequestBody AddSectionRequest request
    ) {
        lineCommander.addSection(request.toCommand(lineId));
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteLine(
            @PathVariable Long lineId,
            @RequestParam Long stationId
    ) {
        lineCommander.deleteSection(new LineCommand.DeleteSection(lineId, stationId));
        return ResponseEntity.noContent().build();
    }
}
