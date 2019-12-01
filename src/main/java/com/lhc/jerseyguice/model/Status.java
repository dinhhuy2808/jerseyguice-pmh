package com.lhc.jerseyguice.model;

import com.lhc.jerseyguice.annotation.KeyColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Status {
	@KeyColumn
	private Integer status_id;
	private String description;

}      
