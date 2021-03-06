package edu.neu.webapp.graphiccodegen.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the developer database table.
 * 
 */
@Entity
public class Developer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String email;

	private String firstName;
	
	private String lastName;

	//bi-directional many-to-one association to Application
	@OneToMany(mappedBy="developer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Application> applications = new ArrayList<Application>();

	public Developer() {
	}
	public Developer(String email,String firstName,String lastName) {
		this.firstName= firstName;
		this.lastName= lastName;
		this.email= email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public Application addApplication(Application application) {
		if(getApplications() == null){
			this.applications = new ArrayList<Application>();
		}
		getApplications().add(application);
		application.setDeveloper(this);

		return application;
	}

	public Application removeApplication(Application application) {
		getApplications().remove(application);
		application.setDeveloper(null);

		return application;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}