package atdd.week2.dao;


import atdd.week2.domain.User;
import atdd.week2.dto.UserRequestDTO;
import atdd.week2.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDao {

    @Resource
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
