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
import com.google.inject.servlet.RequestParameters;
import com.lhc.jerseyguice.dao.CartDao;
import com.lhc.jerseyguice.dao.PaymentDao;
import com.lhc.jerseyguice.dao.TreeFolderDao;
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

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentService {

	@Inject
	PaymentDao dao;
	
	@Inject
	CartDao cartDao;


	@GET
	@Path("checkout/{token}")
	public PaymentScreen checkoutLogin(@PathParam("token") String token) {
		Cart cart = new Cart();
		Claims claims = JWTUtil.decodeJWT(token);
		if(claims.getIssuer().equals("LienHoaCac")){
			cart.setUser_id(Integer.parseInt(claims.getId()));
			cart.setPayment_id(0);
			PaymentScreen payment =  dao.getPaymentDetailForCheckOut(Integer.parseInt(claims.getId()));
			if (payment.getTitle() == null) {
				payment.setTitle("PMH00000001");
			} else {
				int count = Integer.parseInt(payment.getTitle().replace("LHC", ""));
				payment.setTitle("PMH"+String.format("%08d", count+1));
			}
			if(payment != null){
				Settingshop setting = new Settingshop();
				setting = (Settingshop) dao.findByKey(setting).get(0);
				if (payment.getTotal() > setting.getFreeShip()) {
					payment.setShipfee(0.0);
				} else {
					payment.setShipfee(setting.getDefaultShip().doubleValue());
				}
				payment.setSettingShop(setting);
				return payment;
			}
		}
		return null;
	}
	@GET
	@Path("check/voucher/{voucher}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String checkVoucher(@PathParam("voucher") String voucher) {
		Voucher v = new Voucher();
		v.setCode(voucher.toUpperCase());
		JSONObject json = new JSONObject();

		List<Voucher> vouchers = new ArrayList<Voucher>();
		vouchers = dao.findByKey(v);
		if (vouchers.isEmpty()) {
			json.put("message", "Voucher không tồn tại");
			return json.toJSONString();
		} else {
			if (vouchers.get(0).getExpired_date() >= Integer.parseInt(Util.getCurrentDate())
					&& vouchers.get(0).getEffective_date() <= Integer.parseInt(Util.getCurrentDate())) {
				json.put("message", vouchers.get(0).getName()+"###"+vouchers.get(0).getPercent());
				return json.toJSONString();
			} else {
				json.put("message", "Voucher đã hết hạn");
				return json.toJSONString();
			}
		}
	}
	
	@GET
	@Path("get-payment-detail/{token}/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, Object> getPaymentDetail(@PathParam("token") String token,@PathParam("id") String paymentId) {
		Payment payment = new Payment();
		payment.setPayment_id(Integer.parseInt(paymentId));
		payment = (Payment) dao.findByKey(payment).get(0);
		Map<String, Object> map = new HashMap<String, Object>();
		Claims claims = JWTUtil.decodeJWT(token);
		if ((JWTUtil.isValidAdminUser(claims))
				|| (JWTUtil.isValidUser(claims)
						&& payment.getUser_id() == Integer.parseInt(claims.getId()))) {
			Cart cart = new Cart();
			cart.setPayment_id(payment.getPayment_id());
			List<Cart> carts = dao.findByKey(cart);
			Settingshop setting = new Settingshop();
			setting.setId(0);
			setting = (Settingshop) dao.findByKey(setting).get(0);
			map.put("payment", payment);
			map.put("carts", carts);
			map.put("setting", setting);
			return map;
		}
		return null;
	}

	@POST
	@Path("{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String create(String content,@PathParam("token") String token) {
		Gson gson = new Gson();
		PaymentScreen paymentScreen = gson.fromJson(content, PaymentScreen.class);
		 
		Payment payment = new Payment();
		Claims claims = JWTUtil.decodeJWT(token);
		if(claims.getIssuer().equals("LienHoaCac")){
			int totalBeforePromotion = 0;
			for (Cart cart : paymentScreen.getCarts()){
				if (cart.getDisct_price() != 0
						&& cart.getPrice() >= cart.getDisct_price()) {
					totalBeforePromotion += cart.getAmount() * cart.getDisct_price();
				} else {
					totalBeforePromotion += cart.getAmount() * cart.getPrice();
				}
			}
			payment.setAddress(paymentScreen.getAddress());
			payment.setCreate_time(Integer.parseInt(Util.getCurrentDate()));
			payment.setName(paymentScreen.getName());
			payment.setNote(paymentScreen.getNote());
			payment.setPay_type(paymentScreen.getType());
			payment.setPhone(paymentScreen.getPhone());
			payment.setSum(new Double(totalBeforePromotion));
			payment.setPromotion(new Double(paymentScreen.getTotal() - totalBeforePromotion));
			payment.setShip(paymentScreen.getShip());
			payment.setShipfee(paymentScreen.getShipfee().intValue());
			payment.setStatus_id(1);
			payment.setTitle(paymentScreen.getTitle());
			payment.setTotal(paymentScreen.getTotal() + paymentScreen.getShipfee());
			payment.setUser_id(Integer.parseInt(claims.getId()));
			payment.setVoucher(paymentScreen.getVoucher());
			
			String paymentId = dao.addThenReturnId(payment);
			for (Cart cart : paymentScreen.getCarts()){
				if (cart.getAmount() != 0) {
					cart.setPayment_id(Integer.parseInt(paymentId));
					cartDao.updatePayment(cart, Integer.parseInt(paymentId));
				}
			}
			
		} else {
			
		}
		return "200";
	}

}
