package uy.org.curso.jpa.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Entity implementation class for Entity: Autor
 *
 */
@Entity
public class Autor2 implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String nombre;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Libro> librosEscritos = new HashSet<Libro>();
	

	public Autor2() {
		super();
	}

	public Autor2(String nombre) {
		super();
		this.nombre = nombre;
	}

	public void agregarLibro(Libro libro) {
		librosEscritos.add(libro);
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Set<Libro> getLibrosEscritos() {
		return librosEscritos;
	}

	public void setLibrosEscritos(Set<Libro> librosEscritos) {
		this.librosEscritos = librosEscritos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Autor2 other = (Autor2) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
