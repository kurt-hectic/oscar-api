package wmo.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
public class Observation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private int variable;
	
	public Observation() {
		this.variable=0;
		this.deployment=null;
	}

	public Observation(int  variable, Deployment deployment) {
		this.variable = variable;
		this.deployment = deployment;
	}
	

	@Valid
	@OneToOne(cascade=CascadeType.ALL)
	private Deployment deployment ;

	public int getVariable() {
		return variable;
	}

	public void setVariable(int variable) {
		this.variable = variable;
	}

	public Deployment getDeployment() {
		return deployment;
	}

	public void setDeployment(Deployment deployment) {
		this.deployment = deployment;
	}


	
}
