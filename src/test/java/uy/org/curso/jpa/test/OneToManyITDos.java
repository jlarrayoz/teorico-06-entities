package uy.org.curso.jpa.test;


import static org.junit.Assert.assertFalse;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uy.org.curso.jpa.domain.Factura;
import uy.org.curso.jpa.domain.Factura2;
import uy.org.curso.jpa.domain.LineaFactura;
import uy.org.curso.jpa.domain.LineaFactura2;

/**
 * Ejemplos básicos - Prueba relacion OneToMany Bidireccional
 */

public class OneToManyITDos {

		
	/**
	 * OneToMany bidireccional con join table -> Cascade ALL
	 * Persist y Update -> presetar atencion a los comentarios del test
	 * Observar y comparar el persist y el remove de la relacion bidireccional y 
	 * comparar con la relacion unidireccional del mismo tipo (JOIN TABLE)
	 */
	@Test
	public void oneToManyPersistRemoveTable() {
		Factura factura = new Factura("Juan Larrayoz");
		
		//Observar este metodo auxiliar. Se encarga de setear la bidireccionalidad
		//Simplifica la asociacion y previene de olvidos
		factura.agregarLinea(new LineaFactura("Escoba de paja"));
		factura.agregarLinea(new LineaFactura("Reloj"));
		factura.agregarLinea(new LineaFactura("Tornillos"));

		//Observar en este punto la cantidad de consultas que se ejecutan contra la BD
		tx.begin();
		em.persist(factura);
		tx.commit();
		
		Long facturaId = factura.getId();
		
		tx.begin();
		em.clear();
		
		Factura f_aux = em.find(Factura.class, facturaId);
		
		em.remove(f_aux);
		tx.commit();
		
		System.out.println("termino ok");
		
		
	}
	
	/**
	 * OneToMany bidirecional con join table -> Cascade ALL
	 * Persist y remove -> presetar atencion a los comentarios del test
	 * En este caso tratamos de optimizar el remove a mano
	 * Comparar con el caso anterior, sobre todo prestar atencion a la cantidad de consulta que se ejecutan
	 * Y tener en cuenta en el caso anterior, la cantidad que se ejecutarian si las lineas de la factura fueran VERY MANY
	 *
	 * Este ejemplo muestra una forma optimizada de eliminar un objeto con una TO MANY
	 */
	@Test
	public void oneToManyPersistRemoveManualTable() {
		Factura factura = new Factura("Juan Larrayoz");
		factura.agregarLinea(new LineaFactura("Escoba de paja"));
		factura.agregarLinea(new LineaFactura("Reloj"));
		factura.agregarLinea(new LineaFactura("Tornillos"));

		//Observar en este punto la cantidad de consultas que se ejecutan contra la BD
		tx.begin();
		em.persist(factura);
		tx.commit();
		
		Long facturaId = factura.getId();
		
		tx.begin();
		em.clear();
		
		Query q = em.createQuery("DELETE LineaFactura l WHERE l.factura.id = :facturaId");
		q.setParameter("facturaId", facturaId);
		q.executeUpdate();
		 
		Factura f_aux = em.find(Factura.class, facturaId);
		em.remove(f_aux);
		tx.commit();
		
		
		assertFalse(em.contains(f_aux));
		
	}
	
	
	/**
	 * OneToMany bidireccional con join column -> Cascade ALL
	 * Persist y remove -> presetar atencion a los comentarios del test
	 * Comparar con el caso anterior
	 * Sin dudas este pareciera ser el caso OPTIMO
	 */
	@Test
	public void oneToManyPersistRemoveManualOptimoTable() {
		Factura2 factura = new Factura2("Juan Larrayoz");
		factura.agregarLinea(new LineaFactura2("Escoba de paja"));
		factura.agregarLinea(new LineaFactura2("Reloj"));
		factura.agregarLinea(new LineaFactura2("Tornillos"));

		//Observar en este punto la cantidad de consultas que se ejecutan contra la BD
		tx.begin();
		em.persist(factura);
		tx.commit();
		
		Long facturaId = factura.getId();
		
		tx.begin();
		em.clear();
		
		Query q = em.createQuery("DELETE LineaFactura2 l WHERE l.factura.id = :facturaId");
		q.setParameter("facturaId", facturaId);
		q.executeUpdate();
		 
		Factura2 f_aux = em.find(Factura2.class, facturaId);
		em.remove(f_aux);
		tx.commit();
		
		assertFalse(em.contains(f_aux));
		
	}
	
	
	/**
	 * EN CONCLUSION. Para relaciones ToMany, utilizar bidireccionalidad y join column
	 * Si son relaciones VERY To Many pensar en optimizar las operaciones, ejemplo, para eliminar
	 */
	
	

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
