package uy.org.curso.jpa.test;


import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uy.org.curso.jpa.domain.Address;
import uy.org.curso.jpa.domain.Address2;
import uy.org.curso.jpa.domain.Address3;
import uy.org.curso.jpa.domain.Address4;
import uy.org.curso.jpa.domain.Customer;
import uy.org.curso.jpa.domain.Customer2;
import uy.org.curso.jpa.domain.Customer3;
import uy.org.curso.jpa.domain.Customer4;

/**
 * Ejemplos básicos - Prueba relacion OneToOne
 */
public class OneToOneIT {
	
	
	/******************************** ONE TO ONE Unidreccional **************************************/
	
	/**
	 * OneToOne unidireccional -> Cascade PERSIT
	 * Persist -> observar como al persistir el Customer se persiste address
	 */
	@Test
	public void persistCascadeAll() {
		Address address = new Address("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer customer = new Customer("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);

		tx.begin();
		em.persist(customer);
		tx.commit();

		assertNotNull(customer.getId());
		assertNotNull(address.getId());
	}	


	/**
	 * OneToOne unidireccional -> Sin Cascade PERSIST
	 * Persist -> observar como al persistir la persona, NO se propaga el persist a address
	 */
	@Test
	public void persistSInCascade() {
		Address2 address = new Address2("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer2 customer = new Customer2("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);

		Exception exception = assertThrows(RollbackException.class, () -> {
			tx.begin();
			em.persist(customer);
			tx.commit();				
		});
		
		String expectedMessage = "object references an unsaved transient instance - save the transient instance before flushing";
		String errorMessage = exception.getCause().getMessage();
		assertTrue(errorMessage.contains(expectedMessage));		
		
	}	
	
	
	/**
	 * OneToOne unidireccional -> Cascade REMOVE
	 * Remove -> observar como al remover el Customer se remueve la  address
	 */
	@Test
	public void removeConCascade() {
		Address address = new Address("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer customer = new Customer("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);

		tx.begin();
		em.persist(customer);
		tx.commit();
		
		//Vacio el EM
		em.clear();
		
		Long customerId = customer.getId();
		Long addressId = customer.getAddress().getId();
		
		tx.begin();
		customer = em.find(Customer.class, customerId);
		em.remove(customer);
		tx.commit();
		
		address = em.find(Address.class, addressId);
		

		assertNull(address);
	}
	
	/**
	 * OneToOne unidireccional -> SIN cascade REMOVE
	 * Remove -> observar como al remover el Customer NO se remueve la  address
	 */
	@Test
	public void removeSinCascade() {
		Address2 address = new Address2("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer2 customer = new Customer2("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);

		tx.begin();
		em.persist(address);
		em.persist(customer);
		tx.commit();
		
		//Vacio el EM
		em.clear();
		
		Long customerId = customer.getId();
		Long addressId = customer.getAddress().getId();
		
		customer = null;
		address = null;
		
		tx.begin();
		customer = em.find(Customer2.class, customerId);
		em.remove(customer);
		tx.commit();
		
		address = em.find(Address2.class, addressId);
		

		assertNotNull(address);
	}
	
	
	
	/******************************** ONE TO ONE Bidireccional **************************************/
	
	/**
	 * OneToOne bidireccional -> Cascade PERSIT
	 * Persist -> observar como se debe enganchar las 2 partes de la relacion
	 */
	@Test
	public void persistBidirectionalCascadeAll() {
		Address3 address = new Address3("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer3 customer = new Customer3("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);
		
		//En este punto seteo la bidireccionalidad de la relacion
		address.setCustomer(customer);

		tx.begin();
		em.persist(customer);
		tx.commit();

		assertNotNull(customer.getId());
		assertNotNull(address.getId());
		assertNotNull(address.getCustomer().getId());
	}	
	
	

	/**
	 * OneToOne bidireccional -> Sin Orphan Removal
	 * Observar que desengancho la relacion (de ambos lados) y address todavia existe en la BD
	 */
	@Test
	public void removeSiOrphanRemoval() {
		Address3 address = new Address3("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer3 customer = new Customer3("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);
		
		//En este punto seteo la bidireccionalidad de la relacion
		address.setCustomer(customer);

		tx.begin();
		em.persist(customer);
		tx.commit();
		
		//Vacio el EM
		em.clear();
		
		Long customerId = customer.getId();
		Long addressId = customer.getAddress().getId();
		
		tx.begin();
		customer = em.find(Customer3.class, customerId);
		//Desengancho la bidireccionalidad
		customer.getAddress().setCustomer(null);
		customer.setAddress(null);
		tx.commit();
		
		address = em.find(Address3.class, addressId);
		

		assertNotNull(address);
	}	
	
	/**
	 * OneToOne bidireccional -> CON Orphan Removal
	 * Observar que desengancho la relacion (de ambos lados) y al hacer commit se borra address
	 */
	@Test
	public void removeConOrphanRemoval() {
		Address4 address = new Address4("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer4 customer = new Customer4("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);
		
		//En este punto seteo la bidireccionalidad de la relacion
		address.setCustomer(customer);

		tx.begin();
		em.persist(customer);
		tx.commit();
		
		//Vacio el EM
		em.clear();
		
		Long customerId = customer.getId();
		Long addressId = customer.getAddress().getId();
		
		tx.begin();
		customer = em.find(Customer4.class, customerId);
		//Desengancho la bidireccionalidad
		customer.getAddress().setCustomer(null);
		customer.setAddress(null);
		tx.commit();
		
		address = em.find(Address4.class, addressId);
		

		assertNull(address);
	}	
	
	
	
	/********************************** Metodos auxiliares ****************************************/

	
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("curso_bse");
	private EntityManager em;
	private EntityTransaction tx;
	
	//Se ejecuta antes de cada test
	@Before
	public void initEntityManager() throws Exception {
		em = emf.createEntityManager();
		tx = em.getTransaction();
	}

	//Se ejecuta luego de cada test
	@After
	public void closeEntityManager() {
		if (em != null) em.close();
	}

	//Se ejecuta una sola vez luego de terminado los test
	@AfterClass
	public static void closeFactory() {
       emf.close();
	}


}
