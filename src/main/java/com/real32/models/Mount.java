package com.real32.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "mounts")
public class Mount {

	public enum Status {
		AVAILABLE, INSTALLED, RETIRED
	}

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

	@OneToMany(mappedBy = "mount", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<ProductionLog> productionLog = new HashSet<>();

	@Enumerated(EnumType.STRING)
	private Status status;

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<Real32Unit> getMountAInstallations() {
		return mountAInstallations;
	}

	public void setMountAInstallations(Set<Real32Unit> mountAInstallations) {
		this.mountAInstallations = mountAInstallations;
	}

	public Set<Real32Unit> getMountBInstallations() {
		return mountBInstallations;
	}

	public void setMountBInstallations(Set<Real32Unit> mountBInstallations) {
		this.mountBInstallations = mountBInstallations;
	}

	public Set<ProductionLog> getProductionLog() {
		return productionLog;
	}

	public void setProductionLog(Set<ProductionLog> productionLog) {
		this.productionLog = productionLog;
	}
}
