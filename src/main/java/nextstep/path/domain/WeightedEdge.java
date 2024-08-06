package nextstep.path.domain;

public interface WeightedEdge<VERTEX> {

    VERTEX getSource();
    VERTEX getTarget();

    double getWeight();

}
