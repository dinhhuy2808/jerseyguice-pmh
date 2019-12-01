package com.lhc.jerseyguice.screenvars;

import com.lhc.jerseyguice.model.Product;
import com.lhc.jerseyguice.model.Size;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

public class CategoryScreen {
	String image;
	String name;
	String price;
	Integer catId;
	String discountPrice;
	boolean isNew;
	boolean isFreeShip;
	Integer productId;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getCatId() {
		return catId;
	}
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public boolean isFreeShip() {
		return isFreeShip;
	}
	public void setFreeShip(boolean isFreeShip) {
		this.isFreeShip = isFreeShip;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	
}      
