package quickbucks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import quickbucks.Job;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface JobRepository extends CrudRepository<Job, Long> {
	@Query(value = "select * from Job j where j.id = :id", nativeQuery = true)
	Job findJobByID(@Param("id") String id);
	
	@Query(value = "select * from Job j where j.title = :key", nativeQuery = true)
	Iterable<Job> findJobByTitle(@Param("key") String key);

	
	
	}
