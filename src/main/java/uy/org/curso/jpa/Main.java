package uy.org.curso.jpa;

import uy.org.curso.jpa.domain.Address;
import uy.org.curso.jpa.domain.Customer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class Main {

    public static void main(String[] args) {

        System.out.println("Inicio - Obtener Factory y pedirle el EntityManager");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("curso_bse");
        EntityManager em = emf.createEntityManager();

        System.out.println("01 - Iniciar transaccion");
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        System.out.println("02 - Ejecutar instrucciones del usuario");
//        Address address = new Address("18 de julio 1234", "Montevideo", "90210", "Uruguay");
//        Customer customer = new Customer("Juan", "Larrayoz", "juan.larrayoz@gmail.com", address);

//        em.persist(customer);
//        em.persist(address);

        Customer customer = em.find(Customer.class, 1L);
        System.out.println(customer);

        System.out.println("03 - Hacer commit de la transaccion");
        tx.commit();

        System.out.println("Fin - Cerrar la factory y el EntityManager");
        em.close();
        emf.close();
    }

    public static Customer singleResultList() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("curso_bse");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Customer> query = em.createQuery("Select c from Customer c where c.lastName = :apellido", Customer.class);
        query.setParameter("apellido", "Larrayoz");

        return query.getSingleResult();
    }
}
