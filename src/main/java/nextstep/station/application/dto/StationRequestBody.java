package nextstep.station.application.dto;

import javax.validation.constraints.NotBlank;

public class StationRequestBody {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }
}
