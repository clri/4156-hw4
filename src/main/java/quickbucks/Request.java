package quickbucks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Request {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id; //naming convention this must be id and not userID
    
    private Integer employee; //id of logged-in user
    private Integer employer; //id of employer
    private Integer job; //id of job
    private String title; //title
    private String msg;
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}


}

