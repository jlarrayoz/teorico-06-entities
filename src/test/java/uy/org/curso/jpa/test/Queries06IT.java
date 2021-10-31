package uy.org.curso.jpa.test;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uy.org.curso.jpa.domain.Address;
import uy.org.curso.jpa.domain.Banco;
import uy.org.curso.jpa.domain.Customer;

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
	
	
	
	@BeforeClass
	public static void inicialiarDatos() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		Address a = new Address("Montevideo", "Uruguay", "Cuareim 1451", "12500");
		Address b = new Address("Tacuarembo", "Uruguay", "Artigas 1234", "5643");
		Address c = new Address("Bs As", "Argentina", "Libertador 9100", "34567");
		
		Customer c1 = new Customer("juan.larrayoz@gmail.com", "Juan", "Larrayoz",a);
		Customer c2 = new Customer("ort@ort.edu.uy", "Jose", "Perez",b);
		Customer c3 = new Customer("roberto@hotmail.com", "Roberto", "Larrayoz", c);
		
		Banco b1 = new Banco("BSE", "151");
		Banco b2 = new Banco("BANCO REPUBLICA", "001");
		
		tx.begin();
		em.persist(a);
		em.persist(b);
		em.persist(c);
		
		em.persist(c1);
		em.persist(c2);
		em.persist(c3);
		
		em.persist(b1);
		em.persist(b2);
		tx.commit();
		em.close();
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

	@Test
	public void resultList() {
		Query query = em.createQuery("Select c from Customer c where c.lastName = :apellido");
		query.setParameter("apellido", "Larrayoz");

		List<Customer> customers = query.getResultList();

		//Existen 2 clientes con apellido Larrayoz
		assertEquals(2, customers.size());
	}

	@Test
	public void resultListTypedQuery() {
		TypedQuery<Customer> query = em.createQuery("Select c from Customer c where c.lastName = :apellido", Customer.class);
		query.setParameter("apellido", "Larrayoz");

		List<Customer> customers = query.getResultList();

		//Existen 2 clientes con apellido Larrayoz
		assertEquals(2, customers.size());
	}

	/**
	 * Validamos que al modificar se incremente en 1 el campo version
	 */
	@Test
	public void versioning() {
		TypedQuery<Banco> query = em.createQuery("Select b from Banco b where b.nombre = :nombre", Banco.class);
		query.setParameter("nombre", "BSE");
		Banco banco = query.getResultStream().findFirst().orElse(null);

		assertEquals(Long.valueOf(0L), banco.getVersion());

		tx.begin();
		banco.setCodigoBCU("555");
		tx.commit();

		em.refresh(banco);

		assertEquals(Long.valueOf(1L), banco.getVersion());
	}
}
