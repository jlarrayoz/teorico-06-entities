package uy.org.curso.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customerdos")
public class Customer2 {
  
	@Id
	@GeneratedValue 	
    private Long id;
	
    private String firstName;
    private String lastName;
    private String email;  
    
    @OneToOne
    private Address2 address;
    
    

	public Customer2() {
		super();
	}


	public Customer2(String firstName, String lastName, String email, Address2 address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Address2 getAddress() {
		return address;
	}


	public void setAddress(Address2 address) {
		this.address = address;
	}
}
