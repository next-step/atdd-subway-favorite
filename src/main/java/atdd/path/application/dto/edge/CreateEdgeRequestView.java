package atdd.path.application.dto.edge;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateEdgeRequestView {
    private Long sourceId;
    private Long targetId;
    private int distance;

    public CreateEdgeRequestView(Long sourceId, Long targetId, int distance) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.distance = distance;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public int getDistance() {
        return distance;
    }
}
