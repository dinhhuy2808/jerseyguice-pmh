package com.lhc.jerseyguice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Discount {
	private Integer product_id;
	private Integer effective_date;
	private Integer expired_date;
	private Double disct_price;

}      
