package helper;

public class PeopleStats {

	int people_id; //people_id
	String name; //name of the person
	float amount; //ID of the debtr object that the person is attached to
	
	public PeopleStats() {
	}
	
	public PeopleStats(int people_id, String name, float amount) {
		this.people_id = people_id;
		this.name = name;
		this.amount = amount;
	}
	 
	
	//setter
	public void setPeople_id(int people_id) {
		this.people_id = people_id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	//getters
	public int getPeople_id() {
		return people_id;
	}
	
	public String getName() {
		return name;
	}
	
	public float getAmount() {
		return amount;
	}
}
