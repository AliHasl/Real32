package com.real32.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "mounts")
public class Mount {

	public enum Status {
		AVAILABLE, INSTALLED, RETIRED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	@Pattern(regexp = "[a-zA-Z0-9][^ijloxIJLOX]{9}")
	@NotNull
	private String serial;

	@NotNull
	private Date manufacturedOn;

	@OneToOne
	@NotNull
	private User manufacturedBy;

	
	@OneToOne
	@Nullable
	private Real32Unit mountAInstallation;

	@OneToOne
	@Nullable
	private Real32Unit mountBInstallation;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "mount_ProductionLog", joinColumns = @JoinColumn(name = "mount_id", referencedColumnName="id"), inverseJoinColumns = @JoinColumn(name = "productionLog_id", referencedColumnName = "id"))
	@NotNull
	private List<ProductionLog> productionLog = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@NotNull
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

	public User getManufacturedBy() {
		return manufacturedBy;
	}

	public void setManufacturedBy(User manufacturedBy) {
		this.manufacturedBy = manufacturedBy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Real32Unit getMountAInstallation() {
		return mountAInstallation;
	}

	public void setMountAInstallation(Real32Unit mountAInstallation) {
		this.mountAInstallation = mountAInstallation;
	}

	public Real32Unit getMountBInstallation() {
		return mountBInstallation;
	}

	public void setMountBInstallation(Real32Unit mountBInstallation) {
		this.mountBInstallation = mountBInstallation;
	}

	public List<ProductionLog> getProductionLog() {
		return productionLog;
	}

	public void setProductionLog(List<ProductionLog> productionLog) {
		this.productionLog = productionLog;
	}
}
