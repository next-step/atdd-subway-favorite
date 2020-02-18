package atdd.path.web;

import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.sql.DataSource;

import static atdd.path.UserConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    //@Autowired
    MockMvc mockMvc;
    private UserController userController;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private UserDao userDao;

    @BeforeEach
    void setUp(){
        userDao = new UserDao(jdbcTemplate);
        userDao.setDataSource(dataSource);
    }

//    @BeforeEach
//    void setUp() {
//       // this.userController = new UserController(userDao);
//        this.userDao = new UserDao(jdbcTemplate);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userDao)).build();
//    }

    ObjectMapper mapper = new ObjectMapper();

    @DisplayName("회원 등록을 한다")
    @Test
    public void createUser() throws Exception {
        User newUser = User.builder()
                .name(USER_NAME)
                .password(USER_PASSWORD)
                .build();
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(USER_NAME))
                .andDo(print());
    }
}
