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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.google.appengine.repackaged.com.google.gson.reflect.TypeToken;
import com.google.inject.servlet.RequestParameters;
import com.lhc.jerseyguice.dao.CartDao;
import com.lhc.jerseyguice.dao.PaymentDao;
import com.lhc.jerseyguice.dao.TreeFolderDao;
import com.lhc.jerseyguice.dao.VoucherDao;
import com.lhc.jerseyguice.jwt.JWTUtil;
import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.model.Category;
import com.lhc.jerseyguice.model.Payment;
import com.lhc.jerseyguice.model.Settingshop;
import com.lhc.jerseyguice.model.Size;
import com.lhc.jerseyguice.model.Treefolder;
import com.lhc.jerseyguice.model.Voucher;
import com.lhc.jerseyguice.screenvars.PaymentScreen;
import com.lhc.util.Util;

import io.jsonwebtoken.Claims;

@Path("/voucher")
@Produces(MediaType.APPLICATION_JSON)
public class VoucherService {

	@Inject
	VoucherDao dao;
	
	@Inject
	CartDao cartDao;


	@GET
	@Path("{token}")
	public List<Voucher> getVouchers(@PathParam("token") String token) {
		Claims claims = JWTUtil.decodeJWT(token);
		Voucher voucher = new Voucher();
		if (JWTUtil.isValidAdminUser(claims)){
			List<Voucher> vouchers = dao.findByKey(voucher);
			return vouchers;
		}
		return null;
	}
	@GET
	@Path("check/{code}/{token}")
	public String checkVouchers(@PathParam("token") String token,@PathParam("code") String code) {
		Claims claims = JWTUtil.decodeJWT(token);
		Voucher voucher = new Voucher();
		if (JWTUtil.isValidAdminUser(claims)){
			voucher.setCode(code.toUpperCase());
			List<Voucher> vouchers = dao.findByKey(voucher);
			if (vouchers.isEmpty()) {
				return "200";
			} 
		}
		return "203";
	}
	
	@PUT
	@Path("{token}")
	public String addVoucher(@PathParam("token") String token, String content) {
		Claims claims = JWTUtil.decodeJWT(token);
		Type listType = new TypeToken<ArrayList<Voucher>>(){}.getType();
		List<Voucher> vouchers = new ArrayList<>();
		Gson gson = new Gson();
		vouchers = (List<Voucher>) gson.fromJson(content, listType);
		if (JWTUtil.isValidAdminUser(claims)){
			for(Voucher voucher: vouchers){
				voucher.setCode(voucher.getCode().toUpperCase());
				dao.add(voucher);
			}
			return "200";
		}
		return null;
	}
	
	@PUT
	@Path("update/{token}")
	public String updateVoucher(@PathParam("token") String token, String content) {
		Claims claims = JWTUtil.decodeJWT(token);
		Type listType = new TypeToken<ArrayList<Voucher>>(){}.getType();
		Voucher voucher = new Voucher();
		Gson gson = new Gson();
		voucher =  gson.fromJson(content, Voucher.class);
		voucher.setCode(voucher.getCode().toUpperCase());
		if (JWTUtil.isValidAdminUser(claims)){
				dao.updateByInputKey(voucher, Arrays.asList("voucher_id"));
			return "200";
		}
		return null;
	}
	@PUT
	@Path("delete/{id}/{token}")
	public String deleteVoucher(@PathParam("token") String token, @PathParam("id") String id) {
		Claims claims = JWTUtil.decodeJWT(token);
		Voucher voucher = new Voucher();
		if (JWTUtil.isValidAdminUser(claims)){
			voucher.setVoucher_id(Integer.parseInt(id));
			dao.deleteByKey(voucher);
			return "200";
		}
		return null;
	}
}
