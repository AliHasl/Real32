package com.real32.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.lang.Nullable;

import com.sun.istack.NotNull;

@Entity
public class ProductionLog {

	public enum Status {
		CREATED, INSTALLED, REMOVED, DESTROYED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private Status status;

	@OneToOne
	@NotNull
	private User user;

	@NotNull
	private Date date;

	@NotNull
	private String log;

	@OneToOne
	@Nullable
	private Real32Unit real32Unit;

	@OneToOne
	@Nullable
	private Mount mount;

	public ProductionLog() {
		super();
	}

	public ProductionLog(Status status, User user, String log) {
		super();
		this.status = status;
		this.user = user;
		this.log = log;
		this.date = new Date();
	}

	public ProductionLog(Status status, User user, Real32Unit real32Unit, String log) {
		super();
		this.status = status;
		this.user = user;
		this.real32Unit = real32Unit;
		this.log = log;
		this.date = new Date();
	}

	public ProductionLog(Status status, User user, Mount mount, String log) {
		super();
		this.status = status;
		this.user = user;
		this.mount = mount;
		this.log = log;
		this.date = new Date();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
