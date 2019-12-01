package com.lhc.jerseyguice.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@XmlRootElement
public class Size {
	private Integer product_id;
	private Integer create_time;
	private Double price;
	private String size;
	private String code;
	private Double disct_price;
	private Integer expired_time;
	private String info;
	private Integer quantity;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public Integer getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getDisct_price() {
		return disct_price;
	}
	public void setDisct_price(Double disct_price) {
		this.disct_price = disct_price;
	}
	public Integer getExpired_time() {
		return expired_time;
	}
	public void setExpired_time(Integer expired_time) {
		this.expired_time = expired_time;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}      
