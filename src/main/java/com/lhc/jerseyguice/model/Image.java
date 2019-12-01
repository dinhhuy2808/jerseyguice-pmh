package com.lhc.jerseyguice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Image {
	private Integer product_id;
	private String url;
	private String type;

}      
