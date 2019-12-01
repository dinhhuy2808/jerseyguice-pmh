package com.lhc.jerseyguice.service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.lhc.jerseyguice.dao.AppPhongThuyDao;
import com.lhc.jerseyguice.model.AppPhongThuyRespone;

@Path("/app-phong-thuy")
@Produces(MediaType.APPLICATION_JSON)
public class AppPhongThuyService {
	
	@Inject
	AppPhongThuyDao dao;
	
	@GET
	@Path("/{yearOfBirth}")
	public String user(@PathParam("yearOfBirth") int yearOfBirth) {
		AppPhongThuyRespone respone = dao.runApp(yearOfBirth);
		
		Gson gsonBuilder = new GsonBuilder().create();
		JsonParser parser = new JsonParser();
		return parser.parse(gsonBuilder.toJson(respone)).toString();
	}
}
