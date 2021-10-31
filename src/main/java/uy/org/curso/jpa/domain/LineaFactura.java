package uy.org.curso.jpa.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

@Entity
public class LineaFactura {
	
	@Id
	@GeneratedValue
	private Long id;
	private String articulo;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinTable(name="fac_linea", joinColumns = @JoinColumn(name="fk_oder"), inverseJoinColumns = @JoinColumn(name="fk_linea"))
	private Factura factura;
	
	
	
	public LineaFactura() {
		super();
	}
	

	public LineaFactura(String articulo) {
		super();
		this.articulo = articulo;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}


	public Factura getFactura() {
		return factura;
	}


	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	

}
