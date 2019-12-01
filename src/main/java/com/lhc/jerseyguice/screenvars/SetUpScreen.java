package com.lhc.jerseyguice.screenvars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SetUpScreen {
	private String phuongthucvanchuyen;
	private String phuongthucthanhtoan;
	private Integer freeship;
	private Integer shipDefault;
	private Map<String, List<String>> treefolders;

}      
