package nextstep.subway.utils;

import nextstep.subway.station.domain.Station;

public final class EntityTestFixture {
    public static Station 신사역 = new Station(1L, "신사역");
    public static Station 강남역 = new Station(2L, "강남역");
    public static Station 판교역 = new Station(3L, "판교역");
    public static Station 광교역 = new Station(4L, "광교역");

    private EntityTestFixture() {
    }
}
