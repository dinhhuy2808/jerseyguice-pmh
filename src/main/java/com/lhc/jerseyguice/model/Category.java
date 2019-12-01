package com.lhc.jerseyguice.model;

import com.lhc.jerseyguice.annotation.KeyColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

public class Category {
	@KeyColumn
	private Integer cat_id;
	private Integer folder_id;
	private String cat_name;
	private String image;
	public Integer getCat_id() {
		return cat_id;
	}
	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}
	public Integer getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(Integer folder_id) {
		this.folder_id = folder_id;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
}      
