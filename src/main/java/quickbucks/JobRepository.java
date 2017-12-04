package quickbucks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;
import quickbucks.Job;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface JobRepository extends CrudRepository<Job, Long> {
	@Query(value = "select * from Job j where j.id = :id", nativeQuery = true)
	Job findJobByID(@Param("id") String id);
	
	/*@Query(value = "select * from Job j where j.jobtitle like CONCAT('%',:key, '%')", nativeQuery = true)
	List<Job> findJobByTitle(@Param("key") String key);
	
	@Query(value = "select * from Job j where j.category like CONCAT('%',:cat, '%')", nativeQuery = true)
	List<Job> findJobByCat(@Param("cat") String cat);
	*/

	@Query(value = "select * from Job j where j.jobtitle like CONCAT('%',:key, '%') AND j.category like CONCAT('%',:cat, '%') limit :start, :num", nativeQuery = true)
	List<Job> findJobByBoth(@Param("key") String key, @Param("cat") String cat, @Param("start") int start, @Param("num") int num);
	
	@Query(value = "select COUNT(*) from Job j where j.jobtitle like CONCAT('%',:key, '%') AND j.category like CONCAT('%',:cat, '%')", nativeQuery = true)
	Integer searchCount(@Param("key") String key, @Param("cat") String cat);
	
	@Query(value = "select * from Job limit :start, :num", nativeQuery = true)
	List<Job> findAllJobs(@Param("start") int start, @Param("num") int num);
	
	

	
	
	}
