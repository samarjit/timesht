package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ADDRESS database table.
 * 
 */
@Entity
@Table(name="ADDRESS")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;
	private String addressid;
	private String addressdesc;
	private List<Person> persons;

    public Address() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false, length=20)
	public String getAddressid() {
		return this.addressid;
	}

	public void setAddressid(String addressid) {
		this.addressid = addressid;
	}


	@Column(length=40)
	public String getAddressdesc() {
		return this.addressdesc;
	}

	public void setAddressdesc(String addressdesc) {
		this.addressdesc = addressdesc;
	}


	//bi-directional many-to-one association to Person
	@OneToMany(mappedBy="address")
	public List<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
}