package nextstep.line.application;

import nextstep.line.domain.Line;

import java.util.List;

public interface LineProvider {
    List<Line> getAllLines();
}
