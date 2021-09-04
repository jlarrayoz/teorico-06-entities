package uy.org.curso.jpa.test;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import uy.org.curso.jpa.domain.Address;
import uy.org.curso.jpa.domain.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertThrows;

/**
 * Ejemplos básicos - Integration Test teórico 06
 */
public class Teorico06IT {
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

	/**
	 * Insertar un customer y su domicilio y verficiar que se le asigne ID
	 * Observar que cuando se persiste los objetos, JPA le asigna un id de forma automatica
	 */
	@Test
	public void persist() {
		Address address = new Address("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer customer = new Customer("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);

		tx.begin();
		em.persist(customer);
		em.persist(address);
		tx.commit();

		assertNotNull(customer.getId());
		assertNotNull(address.getId());
	}

	/**
	 * Buscar una entity en la BD por el id
	 */
	@Test
	public void findById(){
		Customer customer = em.find(Customer.class, 4L);
		assertNotNull(customer);
		assertTrue("El correo debe ser juan.larrayoz@gmail.com",
				customer.getEmail().equalsIgnoreCase("juan.larrayoz@gmail.com"));
	}

	/**
	 * Remover una entity de la base de datos
	 */
	@Test
	public void remove(){
		Customer customer = em.find(Customer.class, 6L);
		assertNotNull(customer);
		assertTrue("Deberia estar atachado al PC", em.contains(customer));

		tx.begin();
		em.remove(customer);
		tx.commit();

		assertNotNull(customer);
		assertFalse("No deberia estar atachado al PC", em.contains(customer));

		Customer customer1 = em.find(Customer.class, customer.getId());

		assertNull(customer1);
	}

	/**
	 * Flush -> sincronizar entidades con la BD
	 */
	@Test
	public void flush(){
		Address address = new Address("18 de julio 1234", "Montevideo", "90210", "Uruguay");
		Customer customer = new Customer("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);

		tx.begin();
		em.persist(customer);
		em.persist(address);
		tx.commit(); //commit implica flush

		Address address1 = new Address("Prueba dir", "Ciudad", "9999", "Pais");
		Customer customer1 = new Customer("Prueba", "Prueba", "prueba@gmail.com", address1);

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			tx.begin();
			em.persist(customer1);
			em.flush(); //flush no implica commit, se sincroniza contenido del PC con la BD
			em.persist(address1);
			tx.commit();
		});
		String expectedMessage = "During synchronization a new object was found through a " +
				"relationship that was not marked cascade PERSIST";
		String errorMessage = exception.getMessage();
		assertTrue(errorMessage.contains(expectedMessage));
	}


	/**
	 * Recarga una entity desde la BD
	 */
	@Test
	public void refresh(){
		Customer customer = em.find(Customer.class, 4L);
		assertEquals(customer.getFirstName(), "Juan");
		customer.setFirstName("Diego");
		em.refresh(customer);
		assertEquals(customer.getFirstName(), "Juan");
	}

	/**
	 * Merge de una entity para atacharla nuevamente al PC
	 */
	@Test
	public void merge(){
		Customer customer = em.find(Customer.class, 5L);
		assertEquals(customer.getFirstName(), "Jose");
		em.clear();

		tx.begin();
		customer.setFirstName("No llega a la bd");
		tx.commit();

		customer = em.find(Customer.class, 5L);
		assertEquals(customer.getFirstName(), "Jose");

		tx.begin();
		customer.setFirstName("Rodolfo");
		em.merge(customer);
		tx.commit();

		em.clear();
		customer = em.find(Customer.class, 5L);
		assertEquals("Rodolfo", customer.getFirstName());
	}

}
