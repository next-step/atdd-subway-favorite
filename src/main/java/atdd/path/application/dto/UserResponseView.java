package atdd.path.application.dto;

import java.io.Serializable;

public class UserResponseView implements Serializable {
  private Long id;
  private String email;
  private String name;
  private String password;

  public UserResponseView(Long id, String email, String name, String password) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.password = password;
  }

  public Long getId() {
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
