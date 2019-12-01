package com.lhc.jerseyguice.model;

import com.lhc.jerseyguice.annotation.KeyColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Voucher {
	@KeyColumn
	private Integer voucher_id;
	private String code;
	private Integer percent;
	private Integer effective_date;
	private Integer expired_date;
	private Integer amount;
	private Integer min;
	private String name;

}      
