package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.line.domain.LineSection;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
public class LineSectionEdge extends DefaultWeightedEdge {
  private final LineSection lineSection;

  public LineSectionEdge(LineSection lineSection) {
    this.lineSection = lineSection;
  }

  public static LineSectionEdge of(LineSection lineSection) {
    return new LineSectionEdge(lineSection);
  }

  @Override
  protected double getWeight() {
    return lineSection.getDistance();
  }
}
