# 두 역이 주어졌을 때 유효한 구간인지를 판별하기 

## 문제 상황 

인수 조건의 요구 사항에 따라 즐겨찾기 생성시에는 파라미터로 두 역이 주어진다. 이때 두 역으로 연결된 유효한 간선(`Section`)이 존재해야 즐겨찾기를 생성할 수 있다. 

따라서 두 역으로 연결된 `Section`을 찾아야 하는데, 구간은 여러개를 연결될 수 있으므로 노선에서 찾아야 유효한 구간을 찾을 수 있다. 

무슨 말이냐면, 지하철 노선도에서 두 역사이에 직접적인 연결 뿐 아니라 여러 개의 구간을 거쳐서 연결될 수도 있기 때문이다. 예를 들어, 역 A에서 역 B로 가는 직접적인 구간도 있지만, A-B, B-C 와 같은 구간이 있을 때 A-C 역시도 유효한 구간이라는 것이다. 만약 Section에 대해서만 개별적으로 가져온다면 이와 같은 **`구간의 연속성`** 을 판별하지 못한다. 

## 접근 방법 

따라서 노선을 찾아오고, 전체 노선을 순회하며, 해당 노선 별로 두 역으로 유효하게 연결된 구간이 있는지를 찾아야 한다. 

현재의 엔티티 모델링은 다음과 같았다. 
```
Line > Sections(일급 컬렉션) > Set<Section>
```  

따라서 Sections에게 두 개의 역을 파라미터로 제공하고 Sections에서 유효한 섹션을 판별하도록 접근했다. 

## 탐색 알고리즘 

문제는 두 역을 던져준다고 손쉽게 찾아지지 않는다는 것이었다. 

쉽게 생각해보면, 
1) 두 역이 단순히 존재만 하고,
2) 각각이 다르다면 

Section은 필연적으로 존재한다고 간주할 수도 있을 것이다. 

그런데 문제는 두 역이 동시에 존재하는지를 판별하기 위해서는, 
`SortedSet`에서의 요소들을 효율적으로 처리해야 하는데, 
이를 위해 `SortedSet`의 복사본을 만들어 요소들을 줄여나가는(reducing) 방식으로 요소의 수를 세거나, 
또는 특정 기준에 따라 동등성을 평가하여 이미 확인된 결과를 바탕으로 비교를 수행하는 알고리즘을 구현해야만 했다.

그러한 방법보다는 단순 탐색으로서 전체 탐색을 진행하는 편이 직관적일 것이라 판단해 DFS 알고리즘을 구현하기로 했다.


```java
/**
 * 주어진 sourceStation과 targetStation을 기반으로 유효한 Section이 있는지 여부를 탐색합니다.
 * 만약 두 스테이션이 동시에 존재하고 각각이 다르다면 섹션이 존재하는 것으로 간주할 수 있습니다.
 * 따라서 동시에 존재하는 로직을 판별하는 로직을 구현하거나 전체 경우의 수를 순회하여 탐색하는 로직 둘 중에 하나가 필요하였습니다.
 * @param sourceStationId
 * @param targetStationId
 * @return
 */
public boolean isContainsBothAsValid(Long sourceStationId, Long targetStationId) {
    if (sourceStationId.equals(targetStationId) || !isContainsAnyStation(sourceStationId) || !isContainsAnyStation(targetStationId)) {
        return false;
    }

    Set<Long> visited = new HashSet<>();
    Deque<Long> stack = new ArrayDeque<>();
    stack.push(sourceStationId);
    visited.add(sourceStationId);

    while (!stack.isEmpty()) {
        Long currentStationId = stack.pop();
        if (currentStationId.equals(targetStationId)) {
            return true;
        }

        for (Section section : sections) {
            if (section.fetchUpStationId().equals(currentStationId) && visited.add(section.fetchDownStationId())) {
                stack.push(section.fetchDownStationId());
            } else if (section.fetchDownStationId().equals(currentStationId) && visited.add(section.fetchUpStationId())) {
                stack.push(section.fetchUpStationId());
            }
        }
    }

    return false;
}
```

알고리즘의 작동원리를 살펴보면 다음과 같다. 

1. 예외 검증: 만약 주어지는 두 역이 같거나, 혹은 둘 중 하나라도 발견되지 않는다면 탐색이 필요 없을 것이므로 해당 상황에서 false를 리턴한다. 
2. DFS 시작: stack이 비어있지 않은 동안, 즉 탐색할 역이 남아있는 동안 순회한다.
3. 상행역일 경우: 현재 역(currentStationId)이 구간의 상행역에 해당하면, 이 코드는 하행역을 다음 방문할 역으로 결정한다. 하행역의 ID를 visited 집합에 추가하며, 추가가 성공적으로 이루어지면(즉, 아직 방문하지 않은 역이라면) 해당 하행역 ID를 stack에 넣는다. 즉, 다음 탐색 대상 역을 설정한다.
4. 하행역일 경우: 반대로 현재 역이 구간의 하행역이라면, 상행역을 다음 방문할 역으로 결정한다. 마찬가지로, 상행역의 ID를 visited에 추가하고, 이전에 방문하지 않았다면 stack에 넣어 다음 탐색 대상으로 설정한다.
5. 탐색의 진행: 이 로직을 통해, 모든 가능한 경로를 따라 탐색이 진행된다. stack에 추가된 역은 차례대로 꺼내져 다시 탐색이 이루어지며, 이 과정은 stack이 비거나 목표 역을 찾을 때까지 반복된다.

예를 들어보자.
   - sourceStationId가 1이고 targetStationId가 4인 경우, stack에는 시작 역인 1이 추가되며, visted에도 1이 추가되어 방문했음을 표시한다.
   - stack에서 1을 꺼내 currentStationId로 설정한다. 1은 targetStationId인 4와 다르므로 탐색을 계속 진행한다.
   - 인접 역 탐색: 이제 1과 연결된 모든 구간을 순회한다. 구간이 `1-2`, `2-3`, `3-4`로 설정되어 있다고 가정하자. 현재 역 1은 구간 1-2의 상행역이므로, 하행역인 2를 stack에 추가하고 visited에도 추가한다. 이제 stack은 [2], visited는 {1, 2}이다. 2가 stack에서 꺼내져, 다음으로 2와 연결된 하행역인 3이 stack에 추가된다. 이 과정이 3에서 4로 이동할 때도 동일하게 진행되고, 최종적으로 stack에는 4가 남게 되며, visited는 {1, 2, 3, 4}가 된다.
   - 종료조건: stack에서 4를 꺼내 currentStationId로 설정한다. targetStationId와 같기 때문에 주어진 두 역이 유효한 구간 내에 연결되어 있음이 확인되어 true를 반환한다. 


## 결론

이와 같은 방식으로 두 역을 파라미터로 제공할 때, 유효한 구간을 판별하는 로직을 구현했다. 이러한 접근으로 직접적인 연결뿐 아니라 여려 구간을 거쳐 간접적인 연결 까지도 탐색할 수 있었다. 

즐겨찾기 생성 프로세스에서 `FavoriteFacade`는 `lineService`에게 유효한 구간이 있는지를 물어보고 그에 대한 결과를 바탕으로 생성 로직을 구현한다. 

```java
public FavoriteCreateResponse create(LoginMember loginMember, FavoriteCreateRequest request) {
    if (!lineService.isProperSectionExist(request.getSourceStationId(), request.getTargetStationId())) {
        throw new FavoriteCreationNotValidException();
    }

    return FavoriteCreateResponse.from(favoriteService.create(request.withMemberId(fetchMemberId(loginMember))));
}
```
