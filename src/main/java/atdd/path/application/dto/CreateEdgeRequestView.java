package atdd.path.application.dto;

public class CreateEdgeRequestView {
    private Long sourceId;
    private Long targetId;
    private int distance;

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
