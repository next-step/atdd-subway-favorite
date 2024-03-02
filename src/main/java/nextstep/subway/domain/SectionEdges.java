package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.graph.DefaultEdge;

public class SectionEdges {

    private final List<Edge> edges;

    public SectionEdges(List<Line> lines) {
        this.edges = lines.stream()
            .flatMap(line -> line.getSections().stream())
            .map(section -> new Edge(section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance()))
            .collect(Collectors.toList());
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

