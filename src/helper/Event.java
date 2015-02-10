package helper;

public class Event {
	
	int id; //primary key for the DB
	String name; //name of the event
	String date; //date of the event
	float cost; //amount of the event
	int payee_id; //id of the person who paid (FK)
	int debtr_id; //id of the debtr the event belongs to (FK)
	int phototaken; //is there a photo for the event
	String photofile; //adress of the photo
	
	//constructors
	public Event() {
	}
	
	public Event(String name, String date, float cost, int payee_id, int debtr_id, int phototaken, String photofile) {
		this.name = name;
		this.date = date;
		this.cost = cost;
		this.payee_id = payee_id;
		this.debtr_id = debtr_id;
		this.phototaken = phototaken;
		this.photofile = photofile;
	}
	
	public Event(int id, String name, String date, float cost, int payee_id, int debtr_id, int phototaken, String photofile) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.cost = cost;
		this.payee_id = payee_id;
		this.debtr_id = debtr_id;
		this.phototaken = phototaken;
		this.photofile = photofile;
	}
	
	
	//setters
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setCost(float cost) {
		this.cost = cost;
	}
	
	public void setPayee_id(int payee_id) {
		this.payee_id = payee_id;
	}
	
	public void setDebtr_id(int debtr_id) {
		this.debtr_id = debtr_id;
	}
	
	public void setPhototaken(int phototaken) {
		this.phototaken = phototaken;
	}
	
	public void setPhotofile(String photofile) {
		this.photofile = photofile;
	}  
	
	
	//Getters
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDate() {
		return date;
	}
	
	public float getCost() {
		return cost;
	}
	
	public int getPayee_id() {
		return payee_id;
	}
	
	public int getDebtr_id() {
		return debtr_id;
	}
	 
	public int getPhototaken() {
		return phototaken;
	}
	
	public String getPhotofile() {
		return photofile;
	} 

}
