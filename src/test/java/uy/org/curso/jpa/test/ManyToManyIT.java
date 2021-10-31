package uy.org.curso.jpa.test;


import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import uy.org.curso.jpa.domain.Autor;
import uy.org.curso.jpa.domain.Autor2;
import uy.org.curso.jpa.domain.Libro;

/**
 * Ejemplos básicos - Prueba relacion ManyToMany
 * 
 * Lectura recomendada:
 * 
 * https://thorben-janssen.com/best-practices-for-many-to-many-associations-with-hibernate-and-jpa/
 * 
 * ADVERTENCIA: Las relaciones toMany por defecto son LAZY.
 * OJO con cambiar esto, puede ser peligroso y traer problemas de performance
 */

public class ManyToManyIT {


	/**
	 * ManyToMany bidireccional mapeada utilizandao java.util.List -> Cascade.ALL
	 * Se elimina un elemento de la coleccion de libros del autor
	 */
	@Test
	public void manyToManyUnidirectionalRemove() {
		Autor autor = new Autor("Curso JEE JAVA");
		autor.agregarLibro(new Libro("Mastering Hibernate"));
		autor.agregarLibro(new Libro("Mastering EclipseLink"));
		autor.agregarLibro(new Libro("Mastering OpenJPA"));
		

		tx.begin();
		em.persist(autor);
		tx.commit();
		
		Long autorId = autor.getId();
		Long libroId = autor.getLibrosEscritos().get(0).getId();
		
		tx.begin();
		em.clear();
		//Observar que se recupera en la bd cuando se consuta por un autor
		Libro aux = em.find(Libro.class, libroId);	
		autor = em.find(Autor.class, autorId);
		
		//Observar en detalle que pasa cuando se remueve un libro
		//Fijarse como borra toda la coleccion (join table) y luego inserta todos nuevamente menos el eliminado
		autor.getLibrosEscritos().remove(aux);	
		tx.commit();
				

		assertFalse(autor.getLibrosEscritos().contains(aux));		
	
	}
	
	/**
	 * ManyToMany bidireccional mapeada utilizandao java.util.Set -> Cascade.ALL
	 * Se elimina un elemento de la coleccion de libros del autor
	 * Idem al ejemplo anterior, solo se cambia el tipo de datos del mapeo
	 */
	@Test
	public void manyToManyUnidirectionalRemoveSet() {
		Autor2 autor = new Autor2("Curso JEE JAVA");
		
		autor.agregarLibro(new Libro("Mastering Hibernate"));
		autor.agregarLibro(new Libro("Mastering OpenJPA"));
		Libro libro = new Libro("Mastering EclipseLink para borrar");
		
		autor.agregarLibro(libro);
		

		tx.begin();
		em.persist(autor);
		tx.commit();
		
		Long autorId = autor.getId();
		Long libroId = libro.getId();
		
		tx.begin();
		em.clear();
		//Observar que se recupera en la bd cuando se consuta por un autor
		Libro aux = em.find(Libro.class, libroId);	
		autor = em.find(Autor2.class, autorId);
		
		//Observar en detalle que pasa cuando se remueve un libro
		//Fijarse como borra en este caso y comparar con el test anterior
		//Ver como este caso es mucho mas performante (no elimina y vuelve a recrear la coleccion)
		autor.getLibrosEscritos().remove(aux);	
		tx.commit();
				

		assertFalse(autor.getLibrosEscritos().contains(aux));		
	
	}
			
	
	/**
	 * ManyToMany bidireccional mapeada utilizandao java.util.Set -> Cascade.ALL
	 * Se el autor y se deberia propagar el remove a los libros
	 */
	@Test
	public void manyToManyUnidirectionalCascadeRemove() {
		Autor2 autor = new Autor2("Curso JEE JAVA");
		
		autor.agregarLibro(new Libro("Mastering Hibernate"));
		autor.agregarLibro(new Libro("Mastering OpenJPA"));
		Libro libro = new Libro("Mastering EclipseLink para borrar");
		
		autor.agregarLibro(libro);
		

		tx.begin();
		em.persist(autor);
		tx.commit();
		
		Long autorId = autor.getId();
		
		tx.begin();
		em.clear();
		Autor2 aux = em.find(Autor2.class, autorId);
		
		//Observar que pasa en el remove con los libros de este autor (Recordar que es cascade.ALL)
		em.remove(aux);
		tx.commit();
				

		assertFalse(em.contains(aux));		
	
	}
	
	
	/**
	 * ManyToMany bidireccional mapeada utilizandao java.util.Set -> Cascade.ALL
	 * Se eliminar el autor y se deberia propagar el remove a los libros. 
	 * Ver que pasa con el libro que esta referenciado por 2 autores
	 */
	@Test
	public void manyToManyUnidirectionalCascadeRemoveDosReferencias() {
		Autor2 autor = new Autor2("Curso JEE JAVA");
		
		autor.agregarLibro(new Libro("Mastering Hibernate"));
		autor.agregarLibro(new Libro("Mastering OpenJPA"));
		
		//Este libro se va a agregar a los 2 autores
		Libro libro = new Libro("Mastering EclipseLink");
		
		autor.agregarLibro(libro);
		
		Autor2 otroAutor = new Autor2("Jose Perez");
		otroAutor.agregarLibro(libro);

		tx.begin();
		em.persist(autor);
		em.persist(otroAutor);
		tx.commit();
		
		Long otroAutorId = otroAutor.getId();
		
		
		Exception exception = assertThrows(RollbackException.class, () -> {
			tx.begin();
			em.clear();
			Autor2 aux = em.find(Autor2.class, otroAutorId);
			
			//Observar que pasa en el remove con el libro que estaba asociado a los 2 autores
			em.remove(aux);
			tx.commit();			
		});
		
		String expectedMessage = "org.hibernate.exception.ConstraintViolationException: could not execute statement";
		String errorMessage = exception.getCause().getMessage();
		assertTrue(errorMessage.contains(expectedMessage));		
		
	
		em.clear();
		Autor2 aux2 = em.find(Autor2.class, otroAutorId);

		assertEquals(1, aux2.getLibrosEscritos().size());		
	
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
