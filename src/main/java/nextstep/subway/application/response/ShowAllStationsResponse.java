package nextstep.subway.application.response;

import nextstep.subway.application.dto.ShowStationDto;

import java.util.List;

public class ShowAllStationsResponse {

    private List<ShowStationDto> stations;

    private ShowAllStationsResponse() {
    }

    private ShowAllStationsResponse(List<ShowStationDto> stationDtos) {
        this.stations = stationDtos;
    }

    public static ShowAllStationsResponse from(List<ShowStationDto> stationDtos) {
        return new ShowAllStationsResponse(stationDtos);
    }

    public List<ShowStationDto> getStations() {
        return stations;
    }

}
