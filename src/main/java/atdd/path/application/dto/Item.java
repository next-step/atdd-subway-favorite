package atdd.path.application.dto;

import atdd.path.domain.Station;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@EqualsAndHashCode(of = {"id", "name"})
public class Item {
    private Long id;
    private String name;

    public static Item of(Long id, String name) {
        return Item.builder().id(id).name(name).build();
    }

    public static List<Item> listOf(List<Station> paths) {
        return paths.stream()
                .map(it -> Item.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
