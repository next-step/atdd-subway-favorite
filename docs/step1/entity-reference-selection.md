# 엔티티 연관관계 매핑 방법 탐구: 직접? 간접? 

## 대상 클래스 

### Favorite 클래스 

### 요구사항 


즐겨찾기 기능은 지하철 앱 사용자가 자주 이용하는 역을 쉽게 접근할 수 있게 해주는 기능이다.
사용자는 출발역과 도착역을 즐겨찾기로 등록할 수 있으며, 이를 통해 빠르게 노선 정보를 조회할 수 있다. 
최근에 추가된 요구 사항에 따라, 즐겨찾기 기능은 개인별로 관리되어야 하며, 각 즐겨찾기 항목은 특정 사용자와 연관된다.
따라서 `Favorite` 엔티티는 `Station` 엔티티와 `Member` (사용자) 엔티티와의 관계를 정의해야 한다.

이러한 관계를 설정하는 방법으로 **직접 참조**와 **간접 참조** 방식이 있다.
직접 참조는 `Favorite` 엔티티가 `Station` 및 `Member` 엔티티의 인스턴스를 직접 포함하는 방식다.
반면, 간접 참조는 `Favorite` 엔티티가 `Station` 및 `Member` 엔티티의 식별자(ID)만을 저장하는 방식이다.

어떤 방식을 결정할 것이며 왜 그러한 결정을 할 것인가? 


## 직접 참조 방식 

직접 참조는 `Favorite` 엔티티가 `Station`과 `Member` 엔티티에 대한 직접적인 참조를 가지는 것을 의미한다.
이 방식은 객체 간의 관계가 명확하고, ORM 프레임워크에서 제공하는 다양한 기능(예: 지연 로딩, 즉시 로딩 등)을 이용할 수 있는 장점이 있다.
특히, `@ManyToOne` 관계를 사용하여 `Station`과 `Member` 엔티티를 참조함으로써, 한 명의 사용자가 여러 즐겨찾기를 가질 수 있고, 한 역이 여러 즐겨찾기의 출발지나 도착지가 될 수 있다.

```java
@Entity
public class Favorite {
	
    @ManyToOne
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
```


현재 도메인 모델링에서 이 방식이 어울린다면 다음과 같은 이유 때문이다. 

1. **객체 지향적 접근**: 직접 참조는 엔티티 간의 관계를 객체 지향적으로 표현할 수 있게 해준다.
2. **ORM 기능 활용**: JPA에서 직접 참조는 지연 로딩, 즉시 로딩 등의 기능을 쉽게 활용할 수 있게 해준다. 개발 편의성과 성능 최적화에 도움이 됩니다.
3. **즐겨찾기 기능의 특성**: 개인별로 관리되는 즐겨찾기 기능의 경우, 사용자와 역 간의 관계가 명확하고 강하게 연결되어 있다. 직접 참조가 직관적으로 이러한 특성을 잘 반영하지 않을까? 



## 간접 참조 방식 

간접 참조는 `Favorite` 엔티티가 `Station`과 `Member` 엔티티의 식별자(ID)를 저장하는 것을 의미한다.
이 방식은 엔티티 간의 결합도를 낮추고, 데이터베이스의 의존성을 줄일 수 있다. 
이런 점을 고려하면 대규모 시스템에서는 확실히 성능 최적화 측면에서 직접 참조보다 유리할 것이다.

그러나, 객체 지향적인 관점에서는 참조의 명확성이 다소 떨어질 수 있고, 관련 엔티티의 로드를 수동으로 관리해야 하는 번거로움이 있다.


```java
@Entity
public class Favorite {
	private Long memberId;
	private Long sourceStationId;
	private Long targetStationId;
}
```


## 결정 -> 간접 참조 방식 

간접 참조 방식을 선택하면 생성시에는 편하지만 조회와 update 시에 관련 테이블을 수동으로 매핑해야 하므로 불편하다. 타 서비스를 어디서 호출 하느냐도 쟁점이 된다. 잘못하면 코드가 복잡해지고 구조가 꼬이기도 쉽다. 

복잡성을 해결하는 한 가지 방법으로 `Facade` 계층을 도입하여 해결해보았다. 

현재 아키텍처 구조는 패키지 구분으로 도메인 컨텍스트가 어느 정도 격리되어 있는 상황이라고 판단했다. 
따라서 타 패키지의 서비스를 호출하는 방식으로 격리된 기조를 유지하는 것이 패키지 구성에 더 유리하다고 생각했다. 
 `Facade`를 도입하면 기존의 3 티어 레이어드 아키텍처보다 좀 더 유연한 관리가 가능해진다.  `Interfaces` > `Application` > `Domain` < `Infrastructure` 의 3티어를 확장한 4티어 계층으로 나누었고, 서비스와 엔티티를 도메인 레이어에서 다루며 `Facade`를 `Application` 계층에서 다뤄, 
타 서비스를 호출하는 역할을 `Facade`가 담당하도록 했다. 


```java

@Component
@RequiredArgsConstructor
public class FavoriteFacade {

	private final FavoriteService favoriteService;
	private final MemberService memberService;
	private final StationService stationService;
	private final LineService lineService;
	
	// ... 
    
	public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
		return favoriteService.findFavorites(fetchMemberId(loginMember))
			.stream()
			.map(this::buildFavoriteResponse)
			.collect(Collectors.toList());
	}

	private FavoriteResponse buildFavoriteResponse(FavoriteInfo favoriteInfo) {
		StationResponse source = stationService.findStation(favoriteInfo.getSourceStationId());
		StationResponse target = stationService.findStation(favoriteInfo.getTargetStationId());
		return FavoriteResponse.of(favoriteInfo.getId(), source, target);
	}

    // ... 
```

## 몇가지 생각해 볼 점

### 대규모 데이터 세트에서 정말 직접 참조 방식이 재앙을 부를까? 

JPA 직접 참조 방식을 잘못 사용하면 수 천만 건 이상의 데이터 규모에서 재앙이 발생한다는 경험담을 심심찮게 듣는다. 

직접 참조가 데이터베이스 조인의 복잡성을 증가시키기 때문에 성능 저하가 심하다는 것이다. 다음과 같은 시나리오를 고려해보았다. 

예를 들어, 한 사용자가 수천 개의 즐겨찾기를 가지고 있고, 이를 조회할 때마다 관련 역 정보를 모두 불러와야 하는 상황이다. 
이때 데이터베이스에 유저 row는 3,000만, 역은 5,000개라고 가정해보자. 이러한 상황에서 만약 모든 사용자가 자신의 즐겨찾기를 동시에 조회한다고 가정하면, 
시스템은 총 3,000만 건의 사용자 데이터와 5,000개의 역 데이터를 처리해야 한다. 
이를 단순 계산해보면, 사용자당 최소 한 번의 즐겨찾기 조회가 발생한다고 할 때, 
최소 3,000만 번의 데이터 처리가 필요하며, 각 사용자가 평균적으로 10개의 즐겨찾기를 가진다고 가정하면,
총 3억 건의 데이터 처리가 필요한 상황이 발생한다.

그런데 생각해보면 이와 같은 극단적인 상황에서는 간접 참조 방식이라고 해서 자연스럽게 해결될 일은 아닐 것이다. 현실적인 시스템에서는 데이터베이스의 순수한 조회 연산으로 해결하지 않는다. 그렇게 본다면 직접 참조 방식이 성능이 떨어진다는 이유로 기피해야 할까?라는 의문도 든다. 

정도의 차이가 어느 정도 될지 실감이 잘 나지 않기 때문에 기회가 된다면 충분히 복잡성을 갖춘 데이터 세트 상황에서 
직접 참조 방식과 간접 참조 방식의 성능 차이를 비교하는 테스트를 진행해보아야겠다. 


### 도메인 이벤트 활용

간접 참조 방식에서 발생할 수 있는 복잡성 중 하나로 엔티티 간의 관계의 불명확성을 들 수 있을 것 같다.
직접 참조 방식에 뚜렷하게 대비되는 점인데, 즉, 특정 엔티티의 상태 변경이 다른 엔티티에 어떤 영향을 미칠지 추적하기 어렵다는 점이다. 
이를 해결하기 위해 **도메인 이벤트**를 사용해볼 수 있겠다. 
도메인 이벤트는 엔티티의 상태 변경을 이벤트로 발행하여, 다른 엔티티나 서비스가 이를 구독하고 반응할 수 있게 한다. 
이렇게 하면 엔티티 간의 결합도를 낮추면서도, 상태 변경의 영향을 깔끔하게 관리할 수 있다.

예를 들어, `Favorite` 엔티티가 추가되거나 삭제될 때, `FavoriteAddedEvent`나 `FavoriteRemovedEvent` 같은 이벤트를 발행하는 것이다.
`InternalListener`라던지 다른 구독 방식을 통해 관련된 엔티티들은 연관 관계에 따라 `Favorite`의 변경에 따른 수정을 처리할 수 있다.
또한 알림이나 추천 등 비즈니스에 대한 부가 로직으로서도 발행을 활용할 수도 있을 것이다.


