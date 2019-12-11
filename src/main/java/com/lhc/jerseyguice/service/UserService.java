/*
 * Copyright (c) 2016 Aberger Software GmbH. All Rights Reserved.
 *               http://www.aberger.at
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.lhc.jerseyguice.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.appengine.repackaged.com.google.api.client.http.HttpResponse;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.lhc.jerseyguice.dao.UserDao;
import com.lhc.jerseyguice.jwt.JWTUtil;
import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.model.User;

import io.jsonwebtoken.Claims;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

	@Inject
	UserDao dao;

	@GET
	@Path("{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<User> users(@Context HttpServletResponse response, @PathParam("token") String token) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Claims claims = JWTUtil.decodeJWT(token);
		if (JWTUtil.isValidAdminUser(claims)) {
			return (List<User>) dao.findByKey(new User());
		} 
		return null;
	}
	@GET
	@Path("get-by-phone/{token}/{phone}")
	@Consumes(MediaType.APPLICATION_JSON)
	public User user( @PathParam("token") String token, @PathParam("phone") String phone) {
		Claims claims = JWTUtil.decodeJWT(token);
		User user = new User();
		user.setPhone(phone);
		List<User> users = dao.findByKey(user);
		
		if ((!users.isEmpty())
				&& (JWTUtil.isValidAdminUser(claims)
						|| (JWTUtil.isValidUser(claims)
								&& Integer.parseInt(claims.getId()) == users.get(0).getUser_id()))) {
			return users.get(0);
		}
		return null;
	}
	@GET
	@Path("{id}")
	public User user(@PathParam("id") int id) {
		User user = new User();
		user.setUser_id(id);
		return (User) dao.findByKey(user).get(0);
	}

	@GET
	@Path("/check/{phone}")
	public boolean user(@PathParam("phone") String phone) {
		User user = new User();
		user.setPhone(phone);
		if (dao.findByKey(user).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@GET
	@Path("/check-payment/{phone}")
	public boolean checkPayment(@PathParam("phone") String phone) {
		User user = new User();
		user.setPhone(phone);
		List<User> list = dao.findByKey(user);
		if (list.size() > 0 && !list.get(0).getPassword().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	@GET
	@Path("/isAdmin/{token}")
	public boolean isAdmin(@PathParam("token") String token) {
		Claims claims = JWTUtil.decodeJWT(token);
		if(JWTUtil.isValidAdminUser(claims)){
			return true;
		} 
		return false;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String create(String content) {
		User user = (User) dao.parseFromJSONToObject(content, new User());
		if (!user.getPassword().trim().equals("")){
			String hashedPassword = getMd5(user.getPassword());
			user.setPassword(hashedPassword);
		}
		
		User tempUser =  new User();
		tempUser.setPhone(user.getPhone());
		List<User> list = dao.findByKey(tempUser);
		
		if (list.size() > 0  ){
			if (list.get(0).getPassword().equals("")) {
				dao.updateByInputKey(user, Arrays.asList("phone"));
				return String.valueOf(list.get(0).getUser_id());
			}
		} else {
			String userId = dao.addThenReturnId(user);
			return userId;
		}
		return null;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String login(String content) {
		
		User user = (User) dao.parseFromJSONToObject(content, new User());
		String hashedPassword = getMd5(user.getPassword());
		user.setPassword(hashedPassword);
		List<User> users = dao.findByGivenKey(user,"phone,password");
		JsonObject jsonObject = new JsonObject();
		if (!users.isEmpty()) {
			user = users.get(0);
			String jwtId = Integer.toString(user.getUser_id());
			String jwtIssuer = "LienHoaCac";
			String jwtSubject = Integer.toString(user.getType_id());
			long jwtTimeToLive = Long.parseLong("2592000000");
			String jwt = JWTUtil.createJWT(
	                jwtId, // claim = jti
	                jwtIssuer, // claim = iss
	                jwtSubject, // claim = sub
	                jwtTimeToLive // used to calculate expiration (claim = exp)
	        );
			Cart cart = new Cart();
			cart.setUser_id(user.getUser_id());
			cart.setPayment_id(0);
			
			jsonObject.addProperty("name", user.getFirstname() + " " + user.getLastname());
			jsonObject.addProperty("gender", user.getGender());
			jsonObject.addProperty("itemInCart", dao.findByKey(cart).size());
			jsonObject.addProperty("token", jwt);
			jsonObject.addProperty("message", "200");

			return jsonObject.toString();
		}
		jsonObject.addProperty("message", "Login Failed");
		return jsonObject.toString();
	}

	@DELETE
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(User user) {
		dao.deleteByKey(user);

		return "200";
	}

	@PUT
	@Path("{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String update(@PathParam("token") String token, String content) {
		User user = new User();
		Gson gson = new Gson();
		user = gson.fromJson(content, User.class);
		Claims claims = JWTUtil.decodeJWT(token);
		User existUser = new User();
		existUser.setUser_id(user.getUser_id());
		List<User> users = dao.findByKey(existUser);
		if ((!users.isEmpty()) && (JWTUtil.isValidAdminUser(claims)
						|| (JWTUtil.isValidUser(claims)
								&& Integer.parseInt(claims.getId()) == user.getUser_id()))) {
			if(Strings.isNullOrEmpty(user.getPassword())) {
				user.setPassword(users.get(0).getPassword());
				dao.updateByInputKey(user, Arrays.asList("user_id"));
			} else {
				String hashedPassword = getMd5(user.getPassword());
				user.setPassword(hashedPassword);
				dao.updateByInputKey(user, Arrays.asList("user_id"));
			}
			return "200";
		}
		return "203";
	}

	public static String getMd5(String input) {
		try {

			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
