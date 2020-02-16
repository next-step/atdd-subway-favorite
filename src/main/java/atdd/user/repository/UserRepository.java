package atdd.user.repository;

import org.springframework.data.repository.CrudRepository;

import atdd.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
