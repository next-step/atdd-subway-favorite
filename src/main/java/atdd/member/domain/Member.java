package atdd.member.domain;

public class Member {

    private long id;
    private String email;
    private String name;
    private String password;

    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Member(long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean isMatchPassword(String password) {
        return !(this.password == null || password == null)
            && this.password.equals(password);
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

    public String getPassword() {
        return password;
    }
}
