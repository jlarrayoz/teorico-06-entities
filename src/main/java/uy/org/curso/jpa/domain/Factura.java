package uy.org.curso.jpa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Factura {
	@Id
	@GeneratedValue
	private Long id;
	private String cliente;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
	private List<LineaFactura> lineas = new ArrayList<LineaFactura>();
	
	public Factura() {
		super();
	}
	
	
	public Factura(String cliente) {
		super();
		this.cliente = cliente;
	}
	
	/**
	 * Metodo auxiliar para agregar una linea y setear la bidireccionalidad
	 * @param linea
	 */
	public void agregarLinea(LineaFactura linea) {
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

	public List<LineaFactura> getLineas() {
		return lineas;
	}

	public void setLineas(List<LineaFactura> lineas) {
		this.lineas = lineas;
	}
	
	

}
