package atdd.path.web;

import atdd.path.application.dto.CreateEdgeRequestView;
import atdd.path.dao.LineDao;
import atdd.path.application.LineService;
import atdd.path.application.dto.CreateLineRequestView;
import atdd.path.application.dto.LineResponseView;
import atdd.path.domain.Line;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static atdd.Constant.*;

@RestController
@RequestMapping(LINE_BASE_URI)
public class LineController {
    private LineDao lineDao;
    private LineService lineService;

    public LineController(LineDao lineDao, LineService lineService) {
        this.lineDao = lineDao;
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody CreateLineRequestView view) {
        Line persistLine = lineDao.save(view.toLine());
        return ResponseEntity.created(URI.create("/lines/" + persistLine.getId())).body(LineResponseView.of(persistLine));
    }

    @GetMapping("{id}")
    public ResponseEntity retrieveLine(@PathVariable Long id) {
        try {
            Line persistLine = lineDao.findById(id);
            return ResponseEntity.ok().body(LineResponseView.of(persistLine));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity showLine() {
        List<Line> persistLines = lineDao.findAll();
        return ResponseEntity.ok().body(LineResponseView.listOf(persistLines));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineDao.deleteById(id);
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
