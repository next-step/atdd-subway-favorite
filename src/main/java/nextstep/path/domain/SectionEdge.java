package nextstep.path.domain;

import nextstep.section.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public Long getSourceVertex() {
        return section.getUpStationId();
    }

    public Long getTargetVertex() {
        return section.getDownStationId();
    }

    public Section toSection() {
        return section;
    }

    @Override
    public double getWeight() {
        return section.getDistance().doubleValue();
    }

    public static SectionEdge from(final Section section) {
        return new SectionEdge(section);
    }



}

