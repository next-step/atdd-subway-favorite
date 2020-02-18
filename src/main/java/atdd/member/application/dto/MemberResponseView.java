package atdd.member.application.dto;

public class MemberResponseView {

    private long id;
    private String email;
    private String name;

    public MemberResponseView() {
    }

    public MemberResponseView(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
