package com.lhc.jerseyguice.screenvars;

import com.lhc.jerseyguice.model.Size;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProducScreen {
	private String name;
	private String newCode;
	private String mang;
	private String tuoi;
	private String mau;
	private Integer minsize;
	private Integer maxsize;
	private String newImg;
	private Size[] size;
	private String description;

}      
