package quickbucks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import quickbucks.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
	
	@Query(value = "select u.id from User u where u.user_email = :email", nativeQuery = true)
	Integer findIDByEmail(@Param("email") String email);
	
	

}
