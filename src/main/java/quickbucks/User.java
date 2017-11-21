package quickbucks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id; //naming convention this must be id and not userID
    
    private String userLastName;
    private String userFirstName;
    private String userEmail;
    private String userPassword; 
    private String userDegree;
    private String userSchool;
    private String userLocation;
    private Date userRegDate;
    
    public User() {
	    userRegDate = new Date(); //today
    }

    public User(String l, String f, String e,String deg, String sch, String lc) {
	    userLastName = l;
	    userFirstName = f;
	    userEmail = e;
	    userDegree = deg;
	    userSchool = sch;
	    userLocation = lc;
	    userRegDate = new Date(); //today
    }

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	//encrypted
	public void setUserPassword(String password) {
		this.userPassword = password;
	}

	public String getUserDegree() {
		return userDegree;
	}
	public void setUserDegree(String userDegree) {
		this.userDegree = userDegree;
	}
	public String getUserSchool() {
		return userSchool;
	}
	public void setUserSchool(String userSchool) {
		this.userSchool = userSchool;
	}
	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	

	/*NOT SINGLE FIELD GET SETTERS*/
	public String getName() {
		return userFirstName + " " + userLastName;
	}


}

