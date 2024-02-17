package nextstep.api.subway.domain.dto.inport;

import lombok.Getter;
import lombok.Setter;
import nextstep.api.subway.interfaces.dto.request.StationCreateRequest;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

@Getter
@Setter
public class StationCreateCommand {
    private String name;

    public static StationCreateCommand from(StationCreateRequest stationCreateRequest) {
        return ModelMapperBasedObjectMapper.convert(stationCreateRequest, StationCreateCommand.class);
    }
}
