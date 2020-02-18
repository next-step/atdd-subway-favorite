package atdd.path.domain;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static atdd.path.dao.UserDao.ID_KEY;
import static atdd.path.dao.UserDao.NAME_KEY;
import static atdd.path.fixture.UserFixture.*;


@ExtendWith(SoftAssertionsExtension.class)
public class UserTest {
    @DisplayName("findById 로 검색된 Data 를 User 로 변환하는지")
    @Test
    public void getUserByFindData(SoftAssertions softly) {
        User user = User.getUserByFindData(getDaoUser());

        softly.assertThat(user.getId()).isEqualTo(1L);
        softly.assertThat(user.getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("User 로 save 하기 위한 parameter 를 만드는지")
    @Test
    public void getSaveParameterByUser(SoftAssertions softly) {
        Map<String, Object> user =  User.getSaveParameterByUser(NEW_USER);

        softly.assertThat(user.get(ID_KEY)).isEqualTo(1L);
        softly.assertThat(user.get(NAME_KEY)).isEqualTo(KIM_NAME);
    }
}


