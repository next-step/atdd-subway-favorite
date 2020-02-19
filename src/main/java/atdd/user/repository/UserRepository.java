package atdd.user.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import atdd.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
