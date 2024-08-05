package nextstep.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChainSorter<DATA, KEY> {

    private final Function<DATA, KEY> source;
    private final Function<DATA, KEY> target;

    public ChainSorter(final Function<DATA, KEY> source, final Function<DATA, KEY> target) {
        this.source = source;
        this.target = target;
    }

    public List<KEY> getSortedIds(final List<DATA> sections) {
        List<KEY> ids = new ArrayList<>();
        Map<KEY, KEY> sourceTargetMap = sections.stream()
                .collect(Collectors.toMap(source, target));
        Set<KEY> targetIds = sections.stream()
                .map(target).collect(Collectors.toSet());

        KEY currentId = getFirstSourceId(sourceTargetMap, targetIds);

        ids.add(currentId);
        for (int i = 0; i < sourceTargetMap.size(); i++) {
            KEY nextId = sourceTargetMap.get(currentId);
            ids.add(nextId);
            currentId = nextId;
        }
        return ids;
    }

    private KEY getFirstSourceId(final Map<KEY, KEY> sourceTargetMap, final Set<KEY> targetIds) {
        return sourceTargetMap.keySet()
                .stream()
                .filter(it->!targetIds.contains(it))
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("첫번째 역을 찾을 수 없습니다."));
    }

}
