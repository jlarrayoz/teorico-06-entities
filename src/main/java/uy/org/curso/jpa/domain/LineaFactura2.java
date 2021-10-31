package uy.org.curso.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LineaFactura2 {
	
	@Id
	@GeneratedValue
	private Long id;
	private String articulo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_factura")
	private Factura2 factura;
	
	
	
	public LineaFactura2() {
		super();
	}
	

	public LineaFactura2(String articulo) {
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


	public Factura2 getFactura() {
		return factura;
	}


	public void setFactura(Factura2 factura) {
		this.factura = factura;
	}
	
	

}
