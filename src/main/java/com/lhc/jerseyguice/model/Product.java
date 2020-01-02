package com.lhc.jerseyguice.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.lhc.jerseyguice.annotation.KeyColumn;


@XmlRootElement
public class Product {
	@KeyColumn
	private Integer product_id;
	private Integer cat_id;
	private Integer create_time;
	private Double price;
	private String name;
	private String size;
	private String image;
	private String description;
	private String code;
	private String menh;
	private String tuoi;
	private String mau;
	private String information;
	private Integer entity;
	private String validFlag;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public Integer getCat_id() {
		return cat_id;
	}
	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMenh() {
		return menh;
	}
	public void setMenh(String menh) {
		this.menh = menh;
	}
	public String getTuoi() {
		return tuoi;
	}
	public void setTuoi(String tuoi) {
		this.tuoi = tuoi;
	}
	public String getMau() {
		return mau;
	}
	public void setMau(String mau) {
		this.mau = mau;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public Integer getEntity() {
		return entity;
	}
	public void setEntity(Integer entity) {
		this.entity = entity;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	
}      
