package uy.org.curso.jpa.test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uy.org.curso.jpa.domain.Address5;
import uy.org.curso.jpa.domain.Address6;
import uy.org.curso.jpa.domain.Customer5;
import uy.org.curso.jpa.domain.Customer6;

/**
 * Ejemplos básicos - Prueba relacion OneToMany
 * 
 * Lectura recomendada:
 * 
 * https://thorben-janssen.com/best-practices-many-one-one-many-associations-mappings/#Avoid_the_mapping_of_huge_to-many_associations
 * 
 */

public class OneToManyIT {
	

	
	
	/**
	 * OneToMany unidireccional con join table -> Cascade ALL
	 * Persist y Update -> presetar atencion a los comentarios del test
	 * Para el par de entidades 5 de deshabilita el cache
	 */
	@Test
	public void oneToManyPersistAndUpdateJoinTable() {
		Address5 address = new Address5("18 de julio", "Montevideo", "90210", "Uruguay");
		Address5 address1 = new Address5("Br Artigas", "Montevideo", "90210", "Uruguay");
		Address5 address2 = new Address5("Ramon anador", "Montevideo", "90210", "Uruguay");

		List<Address5> domicilios = new ArrayList<Address5>();
		domicilios.add(address);
		domicilios.add(address1);
		domicilios.add(address2);
		
		Customer5 customer = new Customer5("Juan", "Larrayoz", "juan.larrayoz@gmail.com", domicilios);

		//Observar en este punto la cantidad de consultas que se ejecutan contra la BD
		tx.begin();
		em.persist(customer);
		tx.commit();
		
		Long customerId = customer.getId();
		
		tx.begin();
		em.clear();
		//Observar que consultas se ejecutan cuando se hace el find y se recupera el customer de la BD
		//Se puede poner un breakpoint aca y luego vijarse el las variables del debug el tipo de la coleccion
		//de direcciones y abrir la coleccion y observar que pasa
		Customer5 aux = em.find(Customer5.class, customerId);
		
		//Observar las consultas que se ejecutan al agregar un nuevo registro
		Address5 address3 = new Address5("Prueba direccion", "Montevideo", "90210", "Uruguay");
		em.persist(address3);
		
		em.flush();
		
		//Observar que consultas se ejecutan en el commit (observar como BORRA las relaciones y las vuelve a crear)
		aux.getAddress().add(address3);
		tx.commit();
	}
	
	/**
	 * OneToMany unidireccional con join column -> Cascade ALL
	 * Este test es para comparar con el caso anterior. Join Table vs Join Column
	 * Persist y Update -> presetar atencion a los comentarios del test
	 * Para el par de entidades 6 de deshabilita el cache
	 */
	@Test
	public void oneToManyPersistAndUpdateJoinColumn() {
		Address6 address = new Address6("18 de julio", "Montevideo", "90210", "Uruguay");
		Address6 address1 = new Address6("Br Artigas", "Montevideo", "90210", "Uruguay");
		Address6 address2 = new Address6("Ramon anador", "Montevideo", "90210", "Uruguay");

		List<Address6> domicilios = new ArrayList<Address6>();
		domicilios.add(address);
		domicilios.add(address1);
		domicilios.add(address2);
		
		Customer6 customer = new Customer6("Juan", "Larrayoz", "juan.larrayoz@gmail.com", domicilios);

		//Observar en este punto la cantidad de consultas que se ejecutan contra la BD y comparar con el test anterior (Join Table)
		tx.begin();
		em.persist(customer);
		tx.commit();
		
		Long customerId = customer.getId();
		
		tx.begin();
		em.clear();
		Customer6 aux = em.find(Customer6.class, customerId);
		
		//Observar en este punto que pasa cuando al customer le pido una address de su coleccion LAZY
		aux.getAddress().get(0);
		
		
		Address6 address3 = new Address6("Prueba direccion", "Montevideo", "90210", "Uruguay");
		em.persist(address3);
		em.flush();
		
		//Observar que consultas se ejecutan en este punto y comparar con el anterior (join table)
		//Prestar atencion a la cantidade de consultas que se ejecutan en cada test en comapracion
		aux.getAddress().add(address3);
		tx.commit();
	}	
	
	/**
	 * OneToMany unidireccional con join table -> Cascade ALL
	 * Remove -> presetar atencion a los comentarios del test
	 * Para el par de entidades 5 de deshabilita el cache
	 */
	@Test
	public void oneToManyRemoveJoinTable() {
		Address5 address = new Address5("18 de julio", "Montevideo", "90210", "Uruguay");
		Address5 address1 = new Address5("Br Artigas", "Montevideo", "90210", "Uruguay");
		Address5 address2 = new Address5("Ramon anador", "Montevideo", "90210", "Uruguay");

		List<Address5> domicilios = new ArrayList<Address5>();
		domicilios.add(address);
		domicilios.add(address1);
		domicilios.add(address2);
		
		Customer5 customer = new Customer5("Juan", "Larrayoz", "juan.larrayoz@gmail.com", domicilios);

		tx.begin();
		em.persist(customer);
		tx.commit();
		
		Long customerId = customer.getId();
		
		tx.begin();
		em.clear();
		Customer5 aux = em.find(Customer5.class, customerId);	
		Long addressId = aux.getAddress().get(0).getId();
		
		//Observar en detalle las consultas que se ejecutan con el remove
		//Se debe verificar que el cascade.all en una toMany provoca que se eliminen todos los registros de la relacion
		em.remove(aux);
		tx.commit();
		em.clear();
		
		Address5 addressAux = em.find(Address5.class,addressId);
		
		//La entidad no deberia estar atachada al PC
		assertFalse(em.contains(aux));
		
		//El domicilio no deberia existir en la BD
		assertNull(addressAux);
	}
	

	/**
	 * OneToMany unidireccional con join column -> Cascade ALL
	 * Remove -> presetar atencion a los comentarios del test
	 * Para el par de entidades 6 de deshabilita el cache
	 */
	@Test
	public void oneToManyRemoveJoinColumn() {
		Address6 address = new Address6("18 de julio", "Montevideo", "90210", "Uruguay");
		Address6 address1 = new Address6("Br Artigas", "Montevideo", "90210", "Uruguay");
		Address6 address2 = new Address6("Ramon anador", "Montevideo", "90210", "Uruguay");

		List<Address6> domicilios = new ArrayList<Address6>();
		domicilios.add(address);
		domicilios.add(address1);
		domicilios.add(address2);
		
		Customer6 customer = new Customer6("Juan", "Larrayoz", "juan.larrayoz@gmail.com", domicilios);

		tx.begin();
		em.persist(customer);
		tx.commit();
		
		Long customerId = customer.getId();
		
		tx.begin();
		em.clear();
		Customer6 aux = em.find(Customer6.class, customerId);	
		Long addressId = aux.getAddress().get(0).getId();
		
		//Observar en detalle las consultas que se ejecutan con el remove
		//Comparar con el caso anterior de remove por join column
		em.remove(aux);
		tx.commit();
		em.clear();
		
		Address5 addressAux = em.find(Address5.class,addressId);
		
		//La entidad no deberia estar atachada al PC
		assertFalse(em.contains(aux));
		
		//El domicilio no deberia existir en la BD
		assertNull(addressAux);
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
