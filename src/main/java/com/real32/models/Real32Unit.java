package com.real32.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
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

	@ManyToMany
	@JoinTable(name = "mountA_History", joinColumns = @JoinColumn(name = "real32_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "mountA_History", referencedColumnName = "id"))
	private Set<Mount> mountA;

	@ManyToMany
	@JoinTable(name = "mountB_History", joinColumns = @JoinColumn(name = "real32_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "mountB_History", referencedColumnName = "id"))
	private Set<Mount> mountB;

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
}
