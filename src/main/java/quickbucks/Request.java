package quickbucks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Request {
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Integer id; //naming convention this must be id and not userID

        private Integer employee; //id of logged-in user
        private Integer employer; //id of employer
        private Integer job; //id of job
        private String title; //title
        private String msg;

        /*new columns*/
        private Boolean employerRead;
        private Boolean employeeRead;
        private Date requestTime;
        private Date decisionTime;
        private Integer decision;

        public Request()
        {
                requestTime = new Date();
                employerRead = false;
                decision = 0; //undecided
        }

        /*
         * decide(): returns False on invalid param, True otherwise (& in that
         * case sets decisionTime, employeeRead, and decision)
         */
        public boolean decide(int decision) {
                if (decision != 1 && decision != 2)
                        return false;
                this.decision = decision;
                this.decisionTime = new Date();
                this.employeeRead = false;
                this.employerRead = true;
                return true;
        }


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

        public void setEmployerRead(Boolean employerRead) {
                this.employerRead = employerRead;
        }
        public void setEmployeeRead(Boolean employeeRead) {
                this.employeeRead = employeeRead;
        }
        public Boolean getEmployerRead() {
                return employerRead;
        }
        public Boolean getEmployeeRead() {
                return employeeRead;
        }
        public Integer getDecision() {
                return decision;
        }
        public Date getDecisionTime() {
                return decisionTime;
        }
        public Date getRequestTime() {
                return requestTime;
        }


}
