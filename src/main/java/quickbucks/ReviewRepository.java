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
        List<Review> lookupReviewByUserID(@Param("id") Integer id);

	@Query(value = "select * from Review r where r.job = :id", nativeQuery = true)
        Review lookupReviewByJobID(@Param("id") Integer id);

        @Query(value = "select * from Review r where r.job = :id and r.author = :author", nativeQuery = true)
        Review lookupReviewByJobAndAuthor(@Param("id") Integer id, @Param("author") Integer author);
		
		
		@Query(value = "SELECT * FROM REVIEW WHERE (id = :id OR employee = :id) AND author!=:id", nativeQuery = true)
        List<Review> getUserReviews(@Param("id") Integer id);
		
		
}
