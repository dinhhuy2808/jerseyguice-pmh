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

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.lhc.jerseyguice.dao.TreeFolderDao;
import com.lhc.jerseyguice.dao.impl.TreeFolderDaoImpl;
import com.lhc.jerseyguice.jwt.JWTUtil;
import com.lhc.jerseyguice.model.Category;
import com.lhc.jerseyguice.model.Description;
import com.lhc.jerseyguice.model.Product;
import com.lhc.jerseyguice.model.Settingshop;
import com.lhc.jerseyguice.model.Size;
import com.lhc.jerseyguice.model.Thuoctinh;
import com.lhc.jerseyguice.model.Treefolder;

import io.jsonwebtoken.Claims;

@Path("/treefolder")
@Produces(MediaType.APPLICATION_JSON)
public class TreeFolderService {

	TreeFolderDao dao = new TreeFolderDaoImpl();

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getTreefolder(@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Gson gson = new Gson();
		
		return gson.toJson(dao.getTreefolderDetail());
	}

	@GET
	@Path("get-setting-shop")
	public String getSettingShop(@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Settingshop settingshop = new Settingshop();
		settingshop.setId(0);
		List<Settingshop> list = dao.findByKey(settingshop);
		Gson gson = new Gson();
		return gson.toJson(list.get(0));
	}
	

	@PUT
	@Path("{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String create(String content, @PathParam("token") String token) {
		System.out.println(content);
		JSONObject json = new JSONObject();
		JSONParser parser = new JSONParser();
		Claims claims = JWTUtil.decodeJWT(token);
		if(JWTUtil.isValidAdminUser(claims)){
			try {
				Object obj = parser.parse(content);
				json = (JSONObject)obj;
				Treefolder treeFolder = new Treefolder();
				Category category = new Category();
				treeFolder.setFolder_name(json.get("folder_name").toString().trim());
				List<Treefolder> list = dao.findByKey(treeFolder);
				
				if (list.isEmpty()) {
					treeFolder.setFolder_name(null);
					list = dao.findByKey(treeFolder);
					list.sort(Comparator.comparing(Treefolder::getIndex).reversed());
					if(list.isEmpty()) {
						treeFolder.setIndex(1);
					} else {
						treeFolder.setIndex(list.get(0).getIndex() + 1);
					}
					treeFolder.setFolder_name(json.get("folder_name").toString());
					category.setFolder_id(Integer.parseInt(dao.addThenReturnId(treeFolder)));
					category.setCat_name(json.get("cat_name").toString());
					dao.add(category);
				} else {
					category.setFolder_id(list.get(0).getFolder_id());
					category.setCat_name(json.get("cat_name").toString());
					dao.add(category);
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return "203";
			}
		} else {
			return "203";
		}
		
		return "200";
	}

	@PUT
	@Path("update-setting-shop/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateSettingShop(String content,@PathParam("token") String token) {
		System.out.println(content);
		Claims claims = JWTUtil.decodeJWT(token);
		if(JWTUtil.isValidAdminUser(claims)){
			Settingshop setting = new Settingshop();
			Gson gson = new Gson();
			setting = gson.fromJson(content, Settingshop.class);
			setting.setId(0);
			dao.updateByInputKey(setting, Arrays.asList("id"));
		} else {
			return "203";
		}
		return "200";
	}
	@DELETE
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(Treefolder treefolder) {
		dao.deleteByKey(treefolder);

		return "200";
	}
	
	@GET
	@Path("check-cat-name/{name}/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String checkCatName(@PathParam("token") String token,@PathParam("name") String name) {
		Claims claims = JWTUtil.decodeJWT(token);
		if(JWTUtil.isValidAdminUser(claims)){
			Category cat = new Category();
			cat.setCat_name(name);
			if (dao.findByKey(cat).size() > 0) {
				return "exist";
			}
		} else {
			return "203";
		}
		return "200";
	}
	
	@PUT
	@Path("update-cat-name/{token}/{treeName}/{catName}/{newCatName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCatName(@PathParam("token") String token, @PathParam("catName") String catName,
			@PathParam("newCatName") String newCatName,@PathParam("treeName") String treeName) {
		Claims claims = JWTUtil.decodeJWT(token);
		if(JWTUtil.isValidAdminUser(claims)){
			Category cat = new Category();
			Gson gson = new Gson();
			if (!checkCatName(token,newCatName).equals("exist")) {
				dao.updateCatName(treeName, catName, newCatName);
			}
		} else {
			return "203";
		}
		return "200";
	}
	
	@PUT
	@Path("update-index/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateIndex(String content,@PathParam("token") String token) {
		Claims claims = JWTUtil.decodeJWT(token);
		if(JWTUtil.isValidAdminUser(claims)){
			Treefolder tree = new Treefolder();
			Gson gson = new Gson();
			tree = gson.fromJson(content, Treefolder.class);
			dao.updateByInputKey(tree, Arrays.asList("folder_name"));
		} else {
			return "203";
		}
		return "200";
	}
	
	@GET
	@Path("delete-cat/{token}/{catName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteCat(@PathParam("token") String token,@PathParam("catName") String catName) {
		Claims claims = JWTUtil.decodeJWT(token);
		if(JWTUtil.isValidAdminUser(claims)){
			Category cat = new Category();
			cat.setCat_name(catName.replace("-", " ").trim());
			List<Category> cats = dao.findByKey(cat);
			cat = cats.get(0);
			
			Product product = new Product();
			product.setCat_id(cat.getCat_id());
			List<Product> products = dao.findByKey(product);
			for (Product read : products) {
				Size size = new Size();
				size.setProduct_id(read.getProduct_id());
				dao.deleteByGivenValue(size);
				
				for (String desc : read.getDescription().split(",")) {
					Description description = new Description();
					description.setDescription_id(Integer.parseInt(desc));
					dao.deleteByGivenValue(description);
				}
				
				Thuoctinh thuoctinh = new Thuoctinh();
				thuoctinh.setProduct_id(read.getProduct_id());
				dao.deleteByGivenValue(thuoctinh);
				
				dao.deleteByKey(read);
			}
			
			dao.deleteByKey(cat);
			cat.setCat_id(null);
			cat.setCat_name(null);
			cat.setImage(null);
			cats = dao.findByKey(cat);
			
			if (cats.size() == 0) {
				Treefolder tree = new Treefolder();
				tree.setFolder_id(cat.getFolder_id());
				dao.deleteByKey(tree);
			}
			
		} else {
			return "203";
		}
		return "200";
	}
}
