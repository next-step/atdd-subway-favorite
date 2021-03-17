package nextstep.subway.map.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.map.dto.MapResponse;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapController {
    private LineService lineService;

    public MapController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/maps")
    public ResponseEntity<MapResponse> showLineDetail(@AuthenticationPrincipal LoginMember loginMember) {
        List<LineResponse> response = lineService.findLineResponses(loginMember);
        return ResponseEntity.ok(new MapResponse(response));
    }
}
