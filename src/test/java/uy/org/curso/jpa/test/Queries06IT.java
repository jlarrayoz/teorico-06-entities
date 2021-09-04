package uy.org.curso.jpa.test;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import uy.org.curso.jpa.domain.Address;
import uy.org.curso.jpa.domain.Customer;

import javax.persistence.*;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertThrows;

/**
 * Queries - Integration Test teórico 06
 */
public class Queries06IT {
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
	 * Query que devuelve un unico resultado
	 */
	@Test
	public void singleResult() {
		Query query = em.createQuery("Select c from Customer c where c.email = :email");
		query.setParameter("email", "juan.larrayoz@gmail.com");

		try {
			Customer customer = (Customer) query.getSingleResult();
			assertEquals("juan.larrayoz@gmail.com", customer.getEmail());
		}
		catch(NonUniqueResultException | NoResultException ex){
			fail("No se debería haber dado esta situacion");
		}
	}

	/**
	 * Probamos caso cuando no se encuentra resultado
	 */
	@Test
	public void singleResultNoResult() {
		Query query = em.createQuery("Select c from Customer c where c.email = :email");
		query.setParameter("email", "no_existe@gmail.com");

		try {
			Customer customer = (Customer) query.getSingleResult();
			fail("No se debería haber dado esta situacion");
		}
		catch(NonUniqueResultException | NoResultException ex){
			assertTrue(ex instanceof NoResultException);
		}
	}

	/**
	 * Probamos caso cuando se encuentra mas de un resultado
	 */
	@Test
	public void singleResultNoUniqueResult() {
		Query query = em.createQuery("Select c from Customer c where c.lastName = :apellido");
		query.setParameter("apellido", "Larrayoz");

		try {
			Customer customer = (Customer) query.getSingleResult();
			fail("No se debería haber dado esta situacion, hay 2 clientes con el mismo apellido");
		}
		catch(NonUniqueResultException | NoResultException ex){
			assertTrue(ex instanceof NonUniqueResultException);
		}
	}
}
