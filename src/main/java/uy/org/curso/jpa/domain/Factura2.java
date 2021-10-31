package uy.org.curso.jpa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Factura2 {
	@Id
	@GeneratedValue
	private Long id;
	private String cliente;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
	private List<LineaFactura2> lineas = new ArrayList<LineaFactura2>();
	
	public Factura2() {
		super();
	}
	
	
	
	public Factura2(String cliente) {
		super();
		this.cliente = cliente;
	}
	
	public void agregarLinea(LineaFactura2 linea) {
		lineas.add(linea);
		linea.setFactura(this);
	}



	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public List<LineaFactura2> getLineas() {
		return lineas;
	}

	public void setLineas(List<LineaFactura2> lineas) {
		this.lineas = lineas;
	}
	
	

}
