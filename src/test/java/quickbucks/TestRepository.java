package quickbucks;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import quickbucks.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


public interface TestRepository extends CrudRepository<ResetToken, Long> {

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "delete from request", nativeQuery = true)
        void deleteRequests();

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "delete from user", nativeQuery = true)
        void deleteUsers();

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "delete from review", nativeQuery = true)
        void deleteReviews();

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "delete from reset_token", nativeQuery = true)
        void deleteTokens();

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "delete from job", nativeQuery = true)
        void deleteJobs();

}
