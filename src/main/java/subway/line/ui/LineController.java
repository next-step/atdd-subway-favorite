package subway.line.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.LineService;
import subway.line.application.dto.LineCreateRequest;
import subway.line.application.dto.LineModifyRequest;
import subway.line.application.dto.LineRetrieveResponse;
import subway.line.application.dto.SectionCreateRequest;
import subway.line.application.dto.SectionDeleteRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineRetrieveResponse> createLine(@RequestBody @Valid LineCreateRequest lineRequest) {
        LineRetrieveResponse line = lineService.createLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineRetrieveResponse>> retrieveLines() {
        List<LineRetrieveResponse> lineRetrieveRespons = lineService.findAll();
        return ResponseEntity.ok().body(lineRetrieveRespons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineRetrieveResponse> retrieveLine(@PathVariable Long id) {
        LineRetrieveResponse lineRetrieveResponse = lineService.findById(id);
        return ResponseEntity.ok().body(lineRetrieveResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody @Valid LineModifyRequest request) {
        lineService.updateLine(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> appendSection(@PathVariable(name = "id") Long lineId, @RequestBody @Valid SectionCreateRequest request) {
        lineService.appendSection(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable(name = "id") Long lineId, @RequestParam(name = "stationId") Long stationId) {
        SectionDeleteRequest request = SectionDeleteRequest.builder()
                .stationId(stationId)
                .lineId(lineId)
                .build();
        lineService.deleteSection(request);
        return ResponseEntity.noContent().build();
    }


}
