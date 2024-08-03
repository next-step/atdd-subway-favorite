package nextstep.path.repository;

import nextstep.path.domain.SearchPath;
import nextstep.path.payload.ShortestPathResponse;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PathRepository {

    private final Map<SearchPath, ShortestPathResponse> pathMap = new HashMap<>();

    public void put(final SearchPath searchPath,final ShortestPathResponse response) {
        pathMap.put(searchPath , response);
    }

    public ShortestPathResponse get(final SearchPath searchPath) {
        return pathMap.get(searchPath);
    }

    public void removeAll() {
        pathMap.clear();
    }

}
