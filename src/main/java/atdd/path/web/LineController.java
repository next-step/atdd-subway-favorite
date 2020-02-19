package atdd.path.web;

import atdd.path.application.dto.CreateEdgeRequestView;
import atdd.path.application.exception.NoDataException;
import atdd.path.application.LineService;
import atdd.path.application.dto.CreateLineRequestView;
import atdd.path.application.dto.LineResponseView;
import atdd.path.domain.Line;
import atdd.path.repository.LineRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineRepository lineRepository;
    private LineService lineService;

    public LineController(LineRepository lineRepository, LineService lineService) {
        this.lineRepository = lineRepository;
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody CreateLineRequestView view) {
        Line persistLine = lineRepository.save(view.toLine());
        return ResponseEntity.created(URI.create("/lines/" + persistLine.getId())).body(LineResponseView.of(persistLine));
    }

    @GetMapping("{id}")
    public ResponseEntity retrieveLine(@PathVariable Long id) {
        try {
            Line persistLine = lineRepository.findById(id).orElseThrow(NoDataException::new);
            return ResponseEntity.ok().body(LineResponseView.of(persistLine));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity showLine() {
        List<Line> persistLines = new ArrayList<Line>();
        lineRepository.findAll().forEach(persistLines::add);
        return ResponseEntity.ok().body(LineResponseView.listOf(persistLines));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/edges")
    public ResponseEntity createEdge(@PathVariable Long id, @RequestBody CreateEdgeRequestView view) {
        lineService.addEdge(id, view.getSourceId(), view.getTargetId(), view.getDistance());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/edges")
    public ResponseEntity deleteStation(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteStation(id, stationId);
        return ResponseEntity.ok().build();
    }
}
