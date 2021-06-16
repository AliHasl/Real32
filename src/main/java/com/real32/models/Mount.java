package com.real32.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "mounts")
public class Mount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String serial;

	private Date manufacturedOn;

	private String manufacturedBy;

	@ManyToMany(mappedBy = "mountA", fetch = FetchType.LAZY)
	private Set<Real32Unit> mountAInstallations = new HashSet<>();

	@ManyToMany(mappedBy = "mountB", fetch = FetchType.LAZY)
	private Set<Real32Unit> mountBInstallations = new HashSet<>();

	public Mount() {
		super();
	}

	public Mount(String serial) {
		super();
		this.serial = serial;
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

	public String getManufacturedBy() {
		return manufacturedBy;
	}

	public void setManufacturedBy(String manufacturedBy) {
		this.manufacturedBy = manufacturedBy;
	}

	public Set<Real32Unit> getMountAInstallations() {
		return mountAInstallations;
	}

	public Set<Real32Unit> getMountBInstallations() {
		return mountBInstallations;
	}
}
