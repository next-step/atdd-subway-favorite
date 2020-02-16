package atdd.path.application.dto;

public class UserResponseView {
    private Long id;
    private String name;

    public UserResponseView() {
    }

    public UserResponseView(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
