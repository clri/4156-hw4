package quickbucks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Review{
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Integer id; //naming convention this must be id and not userID
		//we need to add the author since both employees and employers write reviews
		//otherwise have to way to differentiate
	private Integer author;
        private Integer employee; //id of logged-in user
        private Integer employer; //id of employer ye
        private Integer job; //id of job
        private Double rating; //title
        private String reviewBody;


        public Integer getId() {
        	return id;
        }
        public void setId(Integer id) {
        	this.id = id;
        }
	public Integer getAuthor() {
        	return author;
        }
        public void setAuthor(Integer author) {
        	this.author = author;
        }
        public Integer getEmployer() {
        	return employer;
        }
        public void setEmployer(Integer employer) {
        	this.employer = employer;
        }
        public Integer getEmployee() {
        	return employee;
        }
        public void setEmployee(Integer employee) {
        	this.employee = employee;
        }
        public Integer getJob() {
        	return job;
        }
        public void setJob(Integer job) {
        	this.job = job;
        }

        public Double getRating() {
        	return rating;
        }
        public void setRating(Double rating) {
        	this.rating = rating;
        }
        public String getReviewBody() {
        	return reviewBody;
        }
        public void setReviewBody(String reviewBody) {
        	this.reviewBody = reviewBody;
        }

}
