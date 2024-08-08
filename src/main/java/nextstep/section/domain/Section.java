package nextstep.section.domain;

import nextstep.common.exception.SectionDistanceNotValidException;
import nextstep.common.response.ErrorCode;
import nextstep.line.domain.Line;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    private boolean first;

    private boolean last;

    protected Section() {}

    public Section(Line line, Long upStationId, Long downStationId, Long distance) {
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public boolean isFirst(){
        return this.first;
    }

    public boolean isLast(){
        return this.last;
    }


    public void updateForNewSection(Section newSection) {

        // 첫번째 역에 추가
        if(first && upStationId.equals(newSection.getDownStationId())){
            first = false;
             newSection.first = true;
             return;
        }

        // 마지막 flag 설정
        if(this.last){
            this.last = false;
            newSection.last = true;
        }

        // 중간 구간 추가 일때만, 거리 조정
        if(this.upStationId.equals(newSection.getUpStationId())){
            if (newSection.distance > distance) {
                throw new SectionDistanceNotValidException(ErrorCode.TOO_LONG_DISTANCE_ADD);
            }

            Long newDistance = this.distance - newSection.distance;
            this.downStationId = newSection.downStationId;
            this.distance = newDistance;
        }


    }

    public void updateNewSection(Long upStationId) {
        if(upStationId.equals(0L)){
            this.first = true;
            this.last = true;
            return;
        }
        this.upStationId = this.downStationId;
        this.downStationId = upStationId;
    }

    public void updateForRemoveSection(Section nextSection, Long removeStationId) {
        // 첫번째 역 셋팅
        if(upStationId.equals(removeStationId)){
            nextSection.first = true;
        }
        // 마지막 역 셋팅
        if(nextSection.last){
            this.last = true;
        }

        // 중간 구간 삭제 일때만, 거리 조정
        if(nextSection.getUpStationId().equals(removeStationId)){
            this.downStationId = nextSection.downStationId;
            this.distance = this.distance + nextSection.distance;
        }


    }


}
