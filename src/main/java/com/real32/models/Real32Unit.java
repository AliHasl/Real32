package com.real32.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.lang.Nullable;

@Entity
@Table(name = "real32units")
public class Real32Unit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	@Pattern(regexp = "[a-zA-Z0-9][^ijloxIJLOX]{9}")
	private String serial;

	@NotNull
	private Date assembledOn;

	@OneToOne
	@NotNull
	private User assembledBy;

	@OneToOne
	@Nullable
	private Mount mountA;

	@OneToOne
	@Nullable
	private Mount mountB;

	@OneToMany(mappedBy = "real32Unit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProductionLog> productionLog = new ArrayList<>();

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

	public User getAssembledBy() {
		return assembledBy;
	}

	public void setAssembledBy(User assembledBy) {
		this.assembledBy = assembledBy;
	}

	public Mount getMountA() {
		return mountA;
	}

	public void setMountA(Mount mountA) {
		this.mountA = mountA;
	}

	public Mount getMountB() {
		return mountB;
	}

	public void setMountB(Mount mountB) {
		this.mountB = mountB;
	}

	public List<ProductionLog> getProductionLog() {
		return productionLog;
	}

	public void setProductionLog(List<ProductionLog> productionLog) {
		this.productionLog = productionLog;
	}
}
