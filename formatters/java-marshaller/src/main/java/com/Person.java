package com;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Person implements Serializable {
	
	private String fname;
	private String lname;
	private Date birthdate;
	
	public void setFname(String s) {
		this.fname = s;
	}
	
	public void setLname(String s) {
		this.lname = s;
	}
	
	public void setBirthdate(Date d) {
		this.birthdate = d;
	}
	
	public String toString() {
		return "[ " + fname + ", " + lname + ", " + birthdate.toString() + " ]";
	}
}
