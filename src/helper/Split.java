package helper;

public class Split {
	
	int id; //Primary key for the splits table in db
	int people_id; //id of the person
	int event_id; //id of the event
	float amount; // amount of the split
	
	//constructors
	public Split() {
	}
	
	public Split(int people_id, int event_id, float amount) {
		this.people_id = people_id;
		this.event_id = event_id;
		this.amount = amount;
	}
	
	public Split(int id, int people_id, int event_id, float amount) {
		this.id = id;
		this.people_id = people_id;
		this.event_id = event_id;
		this.amount = amount;
	}
	
	//setters
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPeople_id(int people_id) {
		this.people_id = people_id;
	}
	
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
	//getters
	public int getId() {
		return id;
	}
	
	public int getPeople_id() {
		return people_id;
	}
	
	public int getEvent_id() {
		return event_id;
	}
	
	public float getAmount() {
		return amount;
	}

}
