package com;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Company implements Serializable {
	
	private String name;
	private String hq;
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void setHq(String s) {
		this.hq = s;
	}

	public String toString() {
		return "[ " + name + ", " + hq + " ]";
	}
}
