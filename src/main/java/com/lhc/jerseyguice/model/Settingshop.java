package com.lhc.jerseyguice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

public class Settingshop {
	private Integer id;
	private String transportMethod;
	private String paymentMethod;
	private Integer freeShip;
	private Integer defaultShip;
	private String noithanh;
	private String ngoaithanh;
	private Integer gianoithanh;
	private Integer giangoaithanh;
	private Integer chanhxe;
	private Integer thuho;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTransportMethod() {
		return transportMethod;
	}
	public void setTransportMethod(String transportMethod) {
		this.transportMethod = transportMethod;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public Integer getFreeShip() {
		return freeShip;
	}
	public void setFreeShip(Integer freeShip) {
		this.freeShip = freeShip;
	}
	public Integer getDefaultShip() {
		return defaultShip;
	}
	public void setDefaultShip(Integer defaultShip) {
		this.defaultShip = defaultShip;
	}
	public String getNoithanh() {
		return noithanh;
	}
	public void setNoithanh(String noithanh) {
		this.noithanh = noithanh;
	}
	public String getNgoaithanh() {
		return ngoaithanh;
	}
	public void setNgoaithanh(String ngoaithanh) {
		this.ngoaithanh = ngoaithanh;
	}
	public Integer getGianoithanh() {
		return gianoithanh;
	}
	public void setGianoithanh(Integer gianoithanh) {
		this.gianoithanh = gianoithanh;
	}
	public Integer getGiangoaithanh() {
		return giangoaithanh;
	}
	public void setGiangoaithanh(Integer giangoaithanh) {
		this.giangoaithanh = giangoaithanh;
	}
	public Integer getChanhxe() {
		return chanhxe;
	}
	public void setChanhxe(Integer chanhxe) {
		this.chanhxe = chanhxe;
	}
	public Integer getThuho() {
		return thuho;
	}
	public void setThuho(Integer thuho) {
		this.thuho = thuho;
	}

	
}      
