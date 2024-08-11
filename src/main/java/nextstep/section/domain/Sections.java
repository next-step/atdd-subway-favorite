package nextstep.section.domain;

import nextstep.common.exception.*;
import nextstep.common.response.ErrorCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Transient
    private final SectionStationSorter sectionStationSorter = new SectionStationSorter();

    public void add(Section section) {
        if(this.sections.isEmpty()){
            section.updateNewSection(0L);
            this.sections.add(section);
            return;
        }

        // 구간이 이미 노선에 포함되있는 경우
        if(getStationIds().contains(section.getDownStationId()) && getStationIds().contains(section.getUpStationId())) {
            throw new ExistStationException(ErrorCode.INVALID_STATION_ADD);
        }
        // 추가되는 거리가 1보다 작은 경우
        if(section.getDistance() < 1){
            throw new SectionDistanceNotValidException(ErrorCode.INVALID_DISTANCE_ADD);

        }

        Section Station = getFirstUpStation();
        // 첫번째 역에 추가
        if(getFirstUpStation().getUpStationId().equals(section.getDownStationId())){
            getFirstUpStation().updateForNewSection(section);
            this.sections.add(section);
            return;
        }

        // 마지막 역에 추가
        if(getLastDownStation().getDownStationId().equals(section.getUpStationId())){
            getLastDownStation().updateForNewSection(section);
            this.sections.add(section);
            return;
        }


        Section origin = getOriginSection(section);
        Long originDownStationId = origin.getDownStationId();

        origin.updateForNewSection(section);
        section.updateNewSection(originDownStationId);

        this.sections.add(section);

    }

    private Section getOriginSection(Section newSection) {
        Section section = null;

        for(Section originSection : this.sections) {
            if(originSection.getUpStationId().equals(newSection.getUpStationId())) {
                section = originSection;
            }
        }

        return section;
    }

    public Collection<Long> getStationIds() {
        List<Long> stationIds = sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());
        stationIds.add(getLastDownStation().getDownStationId());
        return stationIds;
    }

    public Section getLastDownStation() {
        return sections.get(sections.size() - 1);
    }

    public Section getFirstUpStation() {
        return sections.stream()
                .filter(Section::isFirst)
                .findFirst()
                .orElse(null);
    }

    public Section findByUpStationId(Long upStationId) {
        return sections.stream()
                .filter(section -> section.getUpStationId().equals(upStationId))
                .findFirst()
                .orElse(null);
    }
    public Section findByDownStationId(Long downStationId) {
        return sections.stream()
                .filter(section -> section.getDownStationId().equals(downStationId))
                .findFirst()
                .orElse(null);
    }

    public void removeFirstStation(Long stationId) {
        Section previousSection = findByUpStationId(stationId);
        Section nextSection = findByUpStationId(previousSection.getDownStationId());
        previousSection.updateForRemoveSection(nextSection, stationId);
        this.sections.remove(getFirstUpStation());
    }

    public void removeLastStation(Long stationId) {
        Section nextSection = findByDownStationId(stationId);
        Section previousSection = findByDownStationId(nextSection.getUpStationId());
        previousSection.updateForRemoveSection(nextSection, stationId);
        this.sections.remove(getLastDownStation());
    }

    public void removeMiddleStation(Long stationId) {
        Section previousSection = findByDownStationId(stationId);
        Section nextSection = findByUpStationId(stationId);
        previousSection.updateForRemoveSection(nextSection, stationId);
        this.sections.remove(nextSection);
    }
    public void removeStation(Long stationId) {
        // 구간이 1개 이하인경우,
        if (this.sections.size() <= 1) {
            throw new InsufficientStationsException(ErrorCode.INSUFFICIENT_STATION_DELETE);
        }

        // 첫번째 역 삭제
        if(getFirstUpStation().getId().equals(stationId)){
            removeFirstStation(stationId);
            return;
        }

        // 마지막 역 삭제
        if(getLastDownStation().getDownStationId().equals(stationId)){
            removeLastStation(stationId);
            return;
        }

        // 중간역 삭제
        removeMiddleStation(stationId);

    }

    public List<Long> getSortedStationIds() {
        return sectionStationSorter.getSortedStationIds(this.sections);
    }

    public Stream<Section> stream() {
        return sections.stream();
    }
}
