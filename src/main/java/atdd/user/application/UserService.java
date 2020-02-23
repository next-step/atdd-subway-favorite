package atdd.user.application;

import atdd.user.application.dto.LoginResponseView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserDao userDao;
    JwtTokenProvider jwtTokenProvider;

    public UserService(UserDao userDao, JwtTokenProvider jwtTokenProvider){
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseView logIn(LoginUserRequestView loginUser){
        User findUser = userDao.findByEmail(loginUser.getEmail());

        if(!findUser.getPassword().equals(loginUser.getPassword())){
            //throw new IllegalAccessException("암호가 일치하지 않습니다.");

        }

        return new LoginResponseView(jwtTokenProvider.createToken(loginUser.getEmail()),"bearer" );

    }
}
