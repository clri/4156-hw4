package quickbucks;

public class JobDecision{
	private Integer id;
	private Integer decision;
	private String title;
	
	
	public JobDecision(Integer id, Integer decision, String title){
		this.id = id;
		this.decision = decision;
		this.title = title;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDecision(Integer decs) {
                this.decision = decs;
    }
	public Integer getDecision() {
                return decision;
    }
}