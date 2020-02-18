package atdd.path.dao;

import atdd.path.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.*;

import static atdd.path.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class UserDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao(jdbcTemplate);
        userDao.setDataSource(dataSource);
    }

    @DisplayName("회원가입 시 유저가 저장되는지")
    @Test
    public void save() {
        User persistUser = userDao.save(NEW_USER);

        assertThat(persistUser.getId()).isNotNull();
        assertThat(persistUser.getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("id 로 저장된 회원을 찾는지")
    @Test
    public void findById() {
        User savedUser = userDao.save(NEW_USER);

        User persistUser = userDao.findById(savedUser.getId());

        assertThat(persistUser.getId()).isNotNull();
        assertThat(persistUser.getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("findById 로 나온 결과를 User 로 만들어주는지")
    @Test
    public void mapUser() {
        Map<String, Object> savedUser = new HashMap<>();
        savedUser.put("ID", 1);
        savedUser.put("NAME", "김상구");
        savedUser.put("EMAIL", "sgkim94@github.com");
        List<Map<String, Object>> users = Collections.singletonList(savedUser);

        User user = userDao.mapUser(users);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(KIM_NAME);
    }

}
