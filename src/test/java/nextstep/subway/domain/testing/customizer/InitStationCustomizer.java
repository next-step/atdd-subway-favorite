package nextstep.subway.domain.testing.customizer;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import nextstep.subway.domain.entity.station.Station;

import java.util.UUID;

public class InitStationCustomizer implements Customizer {
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> query.getType().equals(Station.class)
                ? new ObjectContainer(new Station(UUID.randomUUID().toString().substring(0, 15)))
                : generator.generate(query, context);
    }
}
