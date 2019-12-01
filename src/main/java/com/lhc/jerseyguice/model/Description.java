package com.lhc.jerseyguice.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.lhc.jerseyguice.annotation.KeyColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@XmlRootElement
public class Description {
	@KeyColumn
	private Integer description_id;
	private String description;
	public Integer getDescription_id() {
		return description_id;
	}
	public void setDescription_id(Integer description_id) {
		this.description_id = description_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}      
