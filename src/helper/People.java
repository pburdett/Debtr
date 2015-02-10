package helper;

public class People {

	int id; //primary key for the People table in the DB
	String name; //name of the person
	int debtr_id; //ID of the debtr object that the person is attached to
	
	public People() {
	}
	
	public People(String name, int debtr_id) {
		this.name = name;
		this.debtr_id = debtr_id;
	}
	
	public People(int id, String name, int debtr_id) {
		this.id = id;
		this.name = name;
		this.debtr_id = debtr_id;
	}
	 
	
	//setter
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDebtr_id(int debtr_id) {
		this.debtr_id = debtr_id;
	}
	
	//getters
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDebtr_id() {
		return debtr_id;
	}
	
}
