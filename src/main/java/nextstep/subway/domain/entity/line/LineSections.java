package nextstep.subway.domain.entity.line;

import lombok.NonNull;
import nextstep.subway.domain.exception.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class LineSections implements Iterable<LineSection> {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    private List<LineSection> data = new ArrayList<>();

    public LineSection get(int idx) {
        return data.get(idx);
    }

    private boolean containsStation(Long stationId) {
        return getAllStationIds().contains(stationId);
    }

    private void arrangePosition() {
        for (int i=0; i<data.size(); i++) {
            data.get(i).changePosition((long) i);
        }
    }

    protected void addSection(LineSection section) {
        if (data.isEmpty()) {
            data.add(section);
            return;
        }

        // 상행역이 노선에 없는데 가장 앞에 추가되지 않는 경우
        if (!containsStation(section.getUpStationId()) && !section.isPrevSectionThan(getFirstSection())) {
            throw new InvalidStationException("상행역이 노선에 존재하지 않는다면 가장 앞 구간으로 추가해야 합니다.");
        }

        // 하행역이 노선에 있는데 가장 앞에 추가되지 않는 경우
        if (containsStation(section.getDownStationId()) && !section.isPrevSectionThan(getFirstSection())) {
            throw new InvalidStationException("하행역이 노선에 존재하다면 가장 앞 구간으로 추가해야 합니다.");
        }

        // 상행역이 존재하는데 가장 앞에 추가되는 경우
        if (containsStation(section.getUpStationId()) && section.isPrevSectionThan(getFirstSection())) {
            throw new InvalidStationException("상행역이 노선에 존재한다면 가장 앞 구간으로 추가할 수 없습니다.");
        }

        addToPosition(section);
        arrangePosition();
    }

    private void addToPosition(LineSection section) {
        if (section.isPrevSectionThan(getFirstSection())) {
            addToFirst(section);
            return;
        }

        if (section.isNextSectionThan(getLastSection())) {
            addToLast(section);
            return;
        }

        addToMiddle(section);
    }

    private void addToFirst(LineSection section) {
        data.add(0, section);
    }

    private void addToLast(LineSection section) {
        data.add(section);
    }

    private void addToMiddle(LineSection section) {
        LineSection originSection = getSectionByUpStationIdOrThrow(section.getUpStationId());
        List<LineSection> splits = originSection.split(section.getDownStationId(), section.getDistance());
        data.addAll(data.indexOf(originSection), splits);
        data.remove(originSection);
    }

    protected void deleteSection(Long stationId) {
        if (size() <= 1) {
            throw new SubwayDomainException(SubwayDomainExceptionType.INVALID_SECTION_SIZE);
        }

        if (!containsStation(stationId)) {
            throw new NotFoundStationOnLineException("노선에 존재하지 않는 역입니다.");
        }

        deleteToPosition(stationId);
        arrangePosition();
    }

    private void deleteToPosition(Long stationId) {
        if (isFirstStation(stationId)) {
            deleteFirst();
            return;
        }

        if (isLastStation(stationId)){
            deleteLast();
            return;
        }

        deleteMiddle(stationId);
    }

    private void deleteFirst() {
        data.remove(0);
    }

    private void deleteLast() {
        data.remove(size() -1);
    }

    private void deleteMiddle(Long stationId) {
        LineSection frontSection = getSectionByDownStationIdOrThrow(stationId);
        LineSection backSection = getSectionByUpStationIdOrThrow(stationId);

        data.add(data.indexOf(frontSection), frontSection.joinNext(backSection));
        data.remove(frontSection);
        data.remove(backSection);
    }

    public int size() {
        return data.size();
    }

    private boolean isFirstStation(Long stationId) {
        return getFirstSection().isSameUpStation(stationId);
    }

    private boolean isLastStation(Long stationId) {
        return getLastSection().isSameDownStation(stationId);
    }

    public LineSection getFirstSection() {
        return data.isEmpty() ? null : data.get(0);
    }

    public LineSection getLastSection() {
        return data.isEmpty() ? null : data.get(size() - 1);
    }

    private LineSection getSectionByUpStationIdOrThrow(Long upStationId) {
        return data.stream()
                .filter(section -> section.isSameUpStation(upStationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundSectionException("상행역이 동일한 section 을 찾을 수 없습니다."));
    }

    private LineSection getSectionByDownStationIdOrThrow(Long downStationId) {
        return data.stream()
                .filter(section -> section.isSameDownStation(downStationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundSectionException("하행역이 동일한 section 을 찾을 수 없습니다."));
    }

    private List<Long> getAllUpStationIds() {
        return data.stream()
                .map(LineSection::getUpStationId)
                .collect(Collectors.toList());
    }

    public List<Long> getAllStationIds() {
        // 모든 상행역 id 가져오기
        List<Long> stationIds = getAllUpStationIds();

        // 마지막 하행역 id 추가
        if (getLastSection() != null) {
            stationIds.add(getLastSection().getDownStationId());
        }
        return stationIds;
    }

    @Override
    @NonNull
    public Iterator<LineSection> iterator() {
        return data.iterator();
    }
}
