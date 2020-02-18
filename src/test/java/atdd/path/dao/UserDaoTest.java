package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.*;

import static atdd.path.dao.UserDao.EMAIL_KEY;
import static atdd.path.dao.UserDao.ID_KEY;
import static atdd.path.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ExtendWith(SoftAssertionsExtension.class)
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
    public void save(SoftAssertions softly) {
        User persistUser = userDao.save(NEW_USER);

        softly.assertThat(persistUser.getId()).isNotNull();
        softly.assertThat(persistUser.getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("id 로 저장된 회원을 찾는지")
    @Test
    public void findById(SoftAssertions softly) {
        User savedUser = userDao.save(NEW_USER);

        User persistUser = userDao.findById(savedUser.getId());

        softly.assertThat(persistUser.getId()).isNotNull();
        softly.assertThat(persistUser.getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("email 로 저장된 회원을 찾는지")
    @Test
    public void findByEmail(SoftAssertions softly) {
        User savedUser = userDao.save(NEW_USER);

        List<Map<String, Object>> persistUser = userDao.findByEmail(savedUser.getEmail());

        Map<String, Object> user = persistUser.get(0);
        softly.assertThat(user.get(ID_KEY)).isNotNull();
        softly.assertThat(user.get(EMAIL_KEY)).isEqualTo(KIM_EMAIL);
    }


    @DisplayName("findById 로 나온 결과를 User 로 만들어주는지")
    @Test
    public void mapUser(SoftAssertions softly) {
        User user = userDao.mapUser(getDaoUsers());

        softly.assertThat(user.getId()).isNotNull();
        softly.assertThat(user.getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("findById 결과가 empty 일 때 NoDataException 예외를 던지는지")
    @Test
    public void MapUserNoDataException() {
        Assertions.assertThrows(NoDataException.class, () -> {
            userDao.checkFindResultIsEmpty(new ArrayList<>());
        });
    }
}
