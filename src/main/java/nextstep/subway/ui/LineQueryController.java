package nextstep.subway.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.domain.service.LineQueryService;

@RestController
@RequestMapping("/lines")
public class LineQueryController {
    private final LineQueryService lineQueryService;

    public LineQueryController(LineQueryService lineQueryService) {
        this.lineQueryService = lineQueryService;
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineQueryService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineQueryService.findLineById(id));
    }
}