package baseClasses;

public class Rating {
	
	private int id;
	private String comment = "";
	private Integer stars = 5;
	private User author = new User();
	
	public Rating(){}
	
	public Rating(int id, String comment, Integer stars, User author){
		this.id = id;
		this.comment = comment;
		this.stars = stars;
		this.author = author;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getStars() {
		return stars;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isComplete(){
		return (stars!=6 && author != null);
	}
	
}