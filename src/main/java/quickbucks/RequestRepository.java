package quickbucks;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import quickbucks.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface RequestRepository extends CrudRepository<Request, Long> {

        @Query(value = "select id, employee, employer, job, msg, title, employer_read, employee_read, request_time, decision_time, decision from"
        + " (select r.*, r.decision_time as ti from Request r"
        + " where r.employee = :uid"
        + " and not r.employee_read"
	+ " and r.decision > 0"
        + " Union"
	+ " select rr.*, rr.request_time as ti from Request rr"
	+ " where rr.employer = :uid"
	+ " and not rr.employer_read"
	+ " and rr.decision = 0) R2"
	+ " order by ti", nativeQuery = true)
        List<Request> getNotifsByUserID(@Param("uid") Integer uid);

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "update request r set r.decision = 2, r.employer_read = 1, r.decision_time = now() where r.job = :jid and r.id != :rid", nativeQuery=true)
        Integer blanketReject(@Param("jid") Integer jid, @Param("rid") Integer rid);


        @Query(value = "select * from Request r where r.id = :id", nativeQuery = true)
	Request findRequestByID(@Param("id") Integer id);

        @Query(value = "select * from Request r where r.job = :jid and r.employee = :eid", nativeQuery = true)
	Request findRequestByJobAndEmployee(@Param("jid") String jid, @Param("eid") Integer eid);

        @Query(value = "select employee from Request r where r.job = :jid and r.decision = 1", nativeQuery = true)
        Integer findAcceptedEmployee(@Param("jid") String jid);

}
