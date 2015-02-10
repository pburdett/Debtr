package helper;

public class Debtr {

	int id; //primary key for the db table
	String name; //name of the Debt'r Account
	String description; //optional description
	String date; //date the debtr was created
	boolean active; //active = true is not "deleted"
	boolean settled; //implies the bill has been settled / paid
	
	//constructors
	public Debtr() {
	}
	
	public Debtr(String name, String description, String date, boolean active, boolean settled) {
		this.name = name;
		this.description = description;
		this.date = date;
		this.active = active;
		this.settled = settled;
	}
	
	public Debtr(int id, String name, String description, String date, boolean active, boolean settled) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.date = date;
		this.active = active;
		this.settled = settled;
	}
	
	//setters
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setSettled(boolean settled) {
		this.settled = settled;
	}
	
	
	//Getters
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	  
	public String getDate() {
		return date;
	}

	public boolean getActive() {
		return active;
	}
	
	public boolean getSettled() {
		return settled;
	}
	
	
}
