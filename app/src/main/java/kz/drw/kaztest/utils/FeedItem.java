package kz.drw.kaztest.utils;

public class FeedItem {

	private int cityId, col, rightcol, wrongcol, inRating;
	private String userName, time, date ,korpus, program, image,city;

	public FeedItem(int cityId, int col, String image,String city, int inRating,int rightcol, int wrongcol, String userName, String time, String date, String korpus, String program) {
		this.cityId = cityId;
		this.inRating = inRating;
		this.col = col;
		this.rightcol = rightcol;
		this.wrongcol = wrongcol;
		this.userName = userName;
		this.image = image;
		this.city = city;
		this.korpus = korpus;
		this.program = program;
		this.time = time;
		this.date = date;
	}

	public FeedItem() {

 }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getKorpus() {
		return korpus;
	}

	public void setKorpus(String korpus) {
		this.korpus = korpus;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getInRating() {
		return inRating;
	}

	public void setInRating(int inRating) {
		this.inRating = inRating;
	}

	public int getRightcol() {
		return rightcol;
	}

	public void setRightcol(int rightcol) {
		this.rightcol = rightcol;
	}

	public int getWrongcol() {
		return wrongcol;
	}

	public void setWrongcol(int wrongcol) {
		this.wrongcol = wrongcol;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}