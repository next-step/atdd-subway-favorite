package nextstep.section.domain;

import nextstep.common.exception.NotFoundFirstSectionException;
import nextstep.common.response.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class SectionStationSorter {
    public List<Long> getSortedStationIds(final List<Section> sections) {
        List<Long> stationIds = new ArrayList<>();

        Long currentStationId = getFirstUpStation(sections).getUpStationId();
        stationIds.add(currentStationId);
        for (Section section : sections) {
            if(!section.isFirst()){
                stationIds.add(section.getUpStationId());
            }
            if(section.isLast()){
                stationIds.add(section.getDownStationId());

            }
        }
        return stationIds;
    }

    public Section getFirstUpStation(final List<Section> sections) {
        return sections.stream()
                .filter(Section::isFirst)
                .findFirst()
                .orElseThrow(()->new NotFoundFirstSectionException(ErrorCode.NOT_FOUND_FIRST_SECTION));
    }
}
