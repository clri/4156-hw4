package quickbucks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import quickbucks.ResetToken;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ResetTokenRepository extends CrudRepository<ResetToken, Long> {

	@Query(value = "select * from Reset_Token r where r.user_email = :user and r.token = :token", nativeQuery = true)
        ResetToken lookupRTByBoth(@Param("user") String user, @Param("token") String token);

}
