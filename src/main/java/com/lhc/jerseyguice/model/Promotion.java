package com.lhc.jerseyguice.model;

import com.lhc.jerseyguice.annotation.KeyColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Promotion {
	@KeyColumn
	private Integer promotion_id;
	private String title;
	private String description;
	private Integer effective_date;
	private Integer expired_date;
	private String image;
	private String seen_flag;
	private Integer user_id;

}      
