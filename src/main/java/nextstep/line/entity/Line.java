package nextstep.line.entity;

import nextstep.section.entity.Sections;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(Long id, String name, String color, Long distance, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = sections;
    }

    public static Line of(String name, String color, Long distance, Sections sections) {
        return new Line(null, name, color, distance, sections);
    }

    public static Line of(Long id, String name, String color, Long distance, Sections sections) {
        return new Line(id, name, color, distance, sections);
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getDistance() {
        return distance;
    }

    public Sections getSections() {
        return this.sections;
    }
}
