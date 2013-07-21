package com.douyang.protaskinate;

public class Comment {
	private long id;
	private String comment;
	
	public Comment(long id, String comment) {
		super();
		this.id = id;
		this.comment = comment;
	}

	public Comment() {
		// TODO Auto-generated constructor stub
	}

	public long getId(){
		return id;
		
	}
	
	public void setId(long l){
		this.id = l;
	}
	
	public String getComment(){
		return this.comment;
	}
	
	public void setComment( String comment){
		this.comment = comment;
		
	}
	
	public String toString(){
		return comment;
	}
}
