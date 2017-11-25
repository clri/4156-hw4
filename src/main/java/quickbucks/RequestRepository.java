package quickbucks;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import quickbucks.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface RequestRepository extends CrudRepository<Request, Long> {

        @Query(value = "select id, employee, employer, job, msg, title, employer_read, employee_read, request_time, decision_time, decision from"
        + "(select r.*, r.decision_time as ti from Request r"
        + "where r.employee = :id"
        + "and not r.employee_read"
	+ "and r.decision > 0) R1"
        + "Union"
	+ "(select rr.*, r.request_time as ti from Request rr"
	+ "where r.employer = :id"
	+ "and not r.employer_read"
	+ "and r.decision = 0) R2"
	+ "order by ti", nativeQuery = true)
        List<Request> getNotifsByUserID(@Param("id") Integer id);

}
