package quickbucks;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String userEmail;
    private String token;

    public void setId(int id) {
            this.id = id;
    }
    public Integer getId() {
            return id;
    }
    public String getUserEmail() {
            return userEmail;
    }
    public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
    }
    public String getToken() {
            return token;
    }
    public void setToken(String token) {
            this.token = token;
    }

}
