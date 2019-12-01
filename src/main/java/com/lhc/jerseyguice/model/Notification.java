package com.lhc.jerseyguice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Notification {
	private Integer notification_id;
	private String title;
	private String description;
	private Integer create_time;
	private String image;
	private Integer user_id;
	private String seen_flag;

}      
