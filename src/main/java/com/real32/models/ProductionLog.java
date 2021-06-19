package com.real32.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProductionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private User user;

	private Date date;

	private String log;

	private Real32Unit real32Unit;

	private Mount mount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Real32Unit getReal32Unit() {
		return real32Unit;
	}

	public void setReal32Unit(Real32Unit real32Unit) {
		this.real32Unit = real32Unit;
	}

	public Mount getMount() {
		return mount;
	}

	public void setMount(Mount mount) {
		this.mount = mount;
	}
}
