package quickbucks;

import org.springframework.data.repository.CrudRepository;

import quickbucks.Job;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface JobRepository extends CrudRepository<Job, Long> {

}
