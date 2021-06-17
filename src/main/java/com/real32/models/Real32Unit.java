package com.real32.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "real32units")
public class Real32Unit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String serial;

	private Date assembledOn;

	private String assembledBy;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "mountA_History", joinColumns = @JoinColumn(name = "real32_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "mount_id", referencedColumnName = "id"))
	private Set<Mount> mountA = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "mountB_History", joinColumns = @JoinColumn(name = "real32_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "mountB_id", referencedColumnName = "id"))
	private Set<Mount> mountB = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Date getAssembledOn() {
		return assembledOn;
	}

	public void setAssembledOn(Date assembledOn) {
		this.assembledOn = assembledOn;
	}

	public String getAssembledBy() {
		return assembledBy;
	}

	public void setAssembledBy(String assembledBy) {
		this.assembledBy = assembledBy;
	}

	public Set<Mount> getMountA() {
		return mountA;
	}

	public void setMountA(Set<Mount> mountA) {
		this.mountA = mountA;
	}

	public Set<Mount> getMountB() {
		return mountB;
	}

	public void setMountB(Set<Mount> mountB) {
		this.mountB = mountB;
	}

}
