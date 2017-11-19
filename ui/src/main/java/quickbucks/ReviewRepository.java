package quickbucks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

import quickbucks.Review;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ReviewRepository extends CrudRepository<Review, Long> {

        @Query(value = "select * from Review r where r.employee = :id", nativeQuery = true)
        List<Review> lookupReviewByUserID(@Param("id") String id);
}
