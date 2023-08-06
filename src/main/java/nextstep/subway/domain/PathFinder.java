package nextstep.subway.domain;

import nextstep.subway.domain.entity.Station;

import java.math.BigInteger;
import java.util.List;

public interface PathFinder {

    List<Station> getPath();

    BigInteger getWeight();
}
