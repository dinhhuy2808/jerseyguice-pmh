package com.lhc.jerseyguice.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@XmlRootElement
public class Thuoctinh {
	private Integer product_id;
	private String mau;
	private String tuoi;
	private String menh;
	private Integer sizefrom;
	private Integer sizeto;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public String getMau() {
		return mau;
	}
	public void setMau(String mau) {
		this.mau = mau;
	}
	public String getTuoi() {
		return tuoi;
	}
	public void setTuoi(String tuoi) {
		this.tuoi = tuoi;
	}
	public String getMenh() {
		return menh;
	}
	public void setMenh(String menh) {
		this.menh = menh;
	}
	public Integer getSizefrom() {
		return sizefrom;
	}
	public void setSizefrom(Integer sizefrom) {
		this.sizefrom = sizefrom;
	}
	public Integer getSizeto() {
		return sizeto;
	}
	public void setSizeto(Integer sizeto) {
		this.sizeto = sizeto;
	}

	
}      
