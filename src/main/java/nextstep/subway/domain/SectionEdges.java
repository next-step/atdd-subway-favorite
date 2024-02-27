package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.DefaultEdge;

public class SectionEdges {

    private final List<Edge> edges;

    public SectionEdges(List<Line> lines) {
        List<Edge> result = new ArrayList<>();
        for (Line line : lines) {
            List<Section> sections = line.getSections();
            for (Section section : sections) {
                result.add(
                    new Edge(section.getUpStation().getId(), section.getDownStation().getId(),
                        section.getDistance()));
            }
        }
        this.edges = result;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public static class Edge extends DefaultEdge {

        private final Long source;
        private final Long target;
        private final int weight;

        public Edge(Long source, Long target, int weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        public Long getSource() {
            return source;
        }

        public Long getTarget() {
            return target;
        }

        public int getWeight() {
            return weight;
        }

    }
}

