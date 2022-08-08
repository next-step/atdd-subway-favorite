package nextstep.station.application.dto;

import nextstep.common.EntitySupplier;
import nextstep.station.domain.Station;

import javax.validation.constraints.NotBlank;

public class StationRequest implements EntitySupplier<Station> {
    @NotBlank
    private String name;

    @Override
    public Station toEntity() {
        return new Station(name);
    }

    public String getName() {
        return name;
    }
}
