package com.lhc.jerseyguice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Wishlist {
	private Integer user_id;
	private Integer product_id;
	private Integer amount;
	private Integer create_time;
	private Double disct_price;
	private Double price;
	private Integer status_id;

}      
