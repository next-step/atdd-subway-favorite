package atdd.path.dao;

import atdd.path.SoftAssertionTest;
import atdd.path.application.dto.user.FindByEmailResponseView;
import atdd.path.application.exception.NoDataException;
import atdd.path.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;

import static atdd.path.fixture.UserFixture.*;

@JdbcTest
public class UserDaoTest extends SoftAssertionTest {
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

        FindByEmailResponseView persistUser = userDao.findByEmail(savedUser.getEmail());

        softly.assertThat(persistUser.getId()).isNotNull();
        softly.assertThat(persistUser.getEmail()).isEqualTo(KIM_EMAIL);
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

    @DisplayName("회원 탈퇴가 성공하는지")
    @Test
    public void deleteById() {
        User savedUser = userDao.save(NEW_USER);

        userDao.deleteById(savedUser.getId());

        Assertions.assertThrows(NoDataException.class, () -> {
            userDao.findById(savedUser.getId());
        });

    }

}
