package com.real32.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="mounts")
public class Mount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String serial;
	
	private Date manufacturedOn;
	
	@OneToOne
	private User manufacturedBy;

	public Mount() {
		super();
	}
	
	public Mount(String serial) {
		super();
		
		this.serial = serial;
		this.manufacturedOn = new Date();
	}
	
	
	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Date getManufacturedOn() {
		return manufacturedOn;
	}

	public void setManufacturedOn(Date manufacturedOn) {
		this.manufacturedOn = manufacturedOn;
	}
	
	public User getManufacturedBy() {
		return manufacturedBy;
	}

	public void setManufacturedBy(User manufacturedBy) {
		this.manufacturedBy = manufacturedBy;
	}
}
