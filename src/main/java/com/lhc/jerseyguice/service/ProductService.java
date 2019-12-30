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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Response;

import org.glassfish.jersey.message.internal.StringBuilderUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.lhc.jerseyguice.dao.ProductDao;
import com.lhc.jerseyguice.dao.impl.ProductDaoImpl;
import com.lhc.jerseyguice.jwt.JWTUtil;
import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.model.Category;
import com.lhc.jerseyguice.model.Description;
import com.lhc.jerseyguice.model.Product;
import com.lhc.jerseyguice.model.Size;
import com.lhc.jerseyguice.model.Thuoctinh;
import com.lhc.jerseyguice.model.Treefolder;
import com.lhc.jerseyguice.screenvars.CategoryScreen;
import com.lhc.util.Util;

import io.jsonwebtoken.Claims;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductService {

	ProductDao dao = new ProductDaoImpl();

	@GET
	@Path("/{product-name}/{purpose}")
	public String getProduct(@PathParam("product-name") String name,@PathParam("purpose") String purpose) {
		System.out.println(name + "--" + purpose);
		JsonObject jsonObject = new JsonObject();
		Product product = new Product();
		product.setName(name.replace("-", " "));
		product = (Product) dao.findByKey(product).get(0);
		Thuoctinh thuoctinh = new Thuoctinh();
		thuoctinh.setProduct_id(product.getProduct_id());
		thuoctinh = (Thuoctinh) dao.findByKey(thuoctinh).get(0);
		Size size = new Size();
		size.setProduct_id(product.getProduct_id());
		List<Size> sizes = dao.findByKey(size);
		String description = "";
		for (String descId : product.getDescription().split(",")) {
			Description desc = new Description();
			desc.setDescription_id(Integer.parseInt(descId));
			List<Description> listDesc = dao.findByKey(desc);
			if (!listDesc.isEmpty()) {
				desc = listDesc.get(0);
				description += desc.getDescription();
			}
		}
		if (purpose.equals("detail")) {
			product.setImage(getImagesUrl(product.getImage()));
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("product", product);
		map.put("sizes", sizes);
		map.put("thuoctinh", thuoctinh);
		map.put("description", description);
		return Util.toJSONString(map);
	}

	@GET
	@Path("/get-by-category/{cat-name}/{page}")
	public String getProductByCategory(@PathParam("cat-name") String catName, @PathParam("page") String page) {
		System.out.println(catName);
		JSONObject object = new JSONObject();
		Gson gson = new Gson();
		Category category = new Category();
		category.setCat_name(catName);
		List<CategoryScreen> list = dao.getProductByCategoryName(catName, page);
		if (!list.isEmpty()) {
			list = list.stream().map(item -> {
				item.setImage(getImagesUrl(item.getImage()).split(";")[0]);
				return item;
			}).collect(Collectors.toList());
			object.put("categoryscreen", list);
			return gson.toJson(list);
		}

		return "203";
	}

	@GET
	@Path("/search/{keyword}/{page}")
	public String search(@PathParam("keyword") String keyword, @PathParam("page") String page) {
		JSONObject object = new JSONObject();
		Gson gson = new Gson();
		List<CategoryScreen> list = dao.search(keyword, page);
		if (!list.isEmpty()) {
			list = list.stream().map(item -> {
				item.setImage(getImagesUrl(item.getImage()).split(";")[0]);
				return item;
			}).collect(Collectors.toList());
			object.put("categoryscreen", list);
			return gson.toJson(list);
		}

		return "203";
	}
	
	public String getImagesUrl(String folderId) {
		List<String> ImagesUrl = new ArrayList<>();
		org.jsoup.nodes.Document doc = null;
		try {
			doc = Jsoup.connect("https://drive.google.com/drive/folders/" + folderId).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("can't fetch from " + folderId);
			return "";
		}
		String title = doc.title();
		for (Element read : doc.getElementsByTag("script")) {
			if (read.toString().contains("\\x5b\\x22" + folderId + "\\x22\\x5d")) {
				List<String> list = new ArrayList<String>();
				String[] element = read.toString().split(",");
				IntStream.range(0, Arrays.asList(element).size() - 1).forEach(i -> {
					if (element[i].contains("\\x5b\\x22" + folderId + "\\x22\\x5d")) {
						list.add(element[i - 1]);
					}
				});
				for (String read2 : list) {
					try {
						System.out.println(read2);
						String url = "https://drive.google.com/uc?export=view&id="
								+ read2.substring(read2.lastIndexOf("\\x5b\\x22"), read2.lastIndexOf("\\x22"))
										.replace("\\x5b\\x22", "").replace("\\x22", "");

						System.out.println(url + ";");
						System.out.println("------------------------");
						ImagesUrl.add(url);
					} catch (StringIndexOutOfBoundsException e) {
						// TODO: handle exception
						continue;
					}

				}

				break;
			}

		}
		return String.join(";", ImagesUrl);
	}

	@PUT
	@Path("/{catName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String create(String content, @Context HttpServletResponse response, @Context HttpServletRequest request,
			@PathParam("catName") String catName) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		Category cat = new Category();
		cat.setCat_name(catName.replace("-", " "));
		List<Category> cats = dao.findByKey(cat);
		if (cats.isEmpty()) {
			return "203";
		} else {
			Product product = new Product();
			product = (Product) dao.parseFromJSONToObject(content, product);
			product.setCat_id(cats.get(0).getCat_id());
			final AtomicInteger atomicInteger = new AtomicInteger(0);
			Collection<String> result = product.getDescription().chars().mapToObj(c -> String.valueOf((char) c))
					.collect(Collectors.groupingBy(c -> atomicInteger.getAndIncrement() / 2000, Collectors.joining()))
					.values();
			String descriptionGeneratedId = "";
			for (String read : result) {
				Description description = new Description();
				description.setDescription(read);
				descriptionGeneratedId += dao.addThenReturnId(description) + ",";
			}
			product.setDescription(descriptionGeneratedId);
			product.setCreate_time(Integer.parseInt(Util.getCurrentDate()));
			Thuoctinh thuoctinh = new Thuoctinh();
			thuoctinh = (Thuoctinh) dao.parseFromJSONToObject(content, thuoctinh);
			List<Size> sizes = dao.parseFromJSONToListOfObject(content, new Size());
			String generatedProductId = dao.addThenReturnId(product);

			for (Size read : sizes) {
				read.setProduct_id(Integer.parseInt(generatedProductId));
				read.setCreate_time(Integer.parseInt(Util.getCurrentDate()));
				dao.add(read);
			}
			thuoctinh.setProduct_id(Integer.parseInt(generatedProductId));
			dao.add(thuoctinh);
		}

		return "200";
	}

	@PUT
	@Path("DV/{catName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String createDV(String content, @Context HttpServletResponse response, @Context HttpServletRequest request,
			@PathParam("catName") String catName) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		Category cat = new Category();
		cat.setCat_name(catName.replace("-", " "));
		List<Category> cats = dao.findByKey(cat);
		if (cats.isEmpty()) {
			return "203";
		} else {
			Product product = new Product();
			product = (Product) dao.parseFromJSONToObject(content, product);
			product.setCat_id(cats.get(0).getCat_id());
			final AtomicInteger atomicInteger = new AtomicInteger(0);
			Collection<String> result = product.getDescription().chars().mapToObj(c -> String.valueOf((char) c))
					.collect(Collectors.groupingBy(c -> atomicInteger.getAndIncrement() / 2000, Collectors.joining()))
					.values();
			String descriptionGeneratedId = "";
			for (String read : result) {
				Description description = new Description();
				description.setDescription(read);
				descriptionGeneratedId += dao.addThenReturnId(description) + ",";
			}
			product.setDescription(descriptionGeneratedId);
			product.setCreate_time(Integer.parseInt(Util.getCurrentDate()));
			Thuoctinh thuoctinh = new Thuoctinh();
			thuoctinh = (Thuoctinh) dao.parseFromJSONToObject(content, thuoctinh);
			String generatedProductId = dao.addThenReturnId(product);
			thuoctinh.setProduct_id(Integer.parseInt(generatedProductId));
			dao.add(thuoctinh);
		}

		return "200";
	}
	
	@PUT
	@Path("edit/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String edit(String content, @Context HttpServletResponse response, @Context HttpServletRequest request,
			@PathParam("token") String token) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		Claims claims = JWTUtil.decodeJWT(token);
		if (!JWTUtil.isValidAdminUser(claims)) {
			return "203";
		} else {
			Product product = new Product();
			product = (Product) dao.parseFromJSONToObject(content, product);
			final AtomicInteger atomicInteger = new AtomicInteger(0);
			Collection<String> result = product.getDescription().chars().mapToObj(c -> String.valueOf((char) c))
					.collect(Collectors.groupingBy(c -> atomicInteger.getAndIncrement() / 2000, Collectors.joining()))
					.values();
			Product existingProduct = new Product();
			existingProduct.setProduct_id(product.getProduct_id());
			List<Product> existingProducts = dao.findByKey(existingProduct);
			existingProduct = existingProducts.get(0);
			for (String read : existingProduct.getDescription().split(",")) {
				Description description = new Description();
				description.setDescription_id(Integer.parseInt(read));
				dao.deleteByKey(description);
			}

			String descriptionGeneratedId = "";
			for (String read : result) {
				Description description = new Description();
				description.setDescription(read);
				descriptionGeneratedId += dao.addThenReturnId(description) + ",";
			}
			product.setDescription(descriptionGeneratedId);
			Thuoctinh thuoctinh = new Thuoctinh();
			thuoctinh = (Thuoctinh) dao.parseFromJSONToObject(content, thuoctinh);
			List<Size> sizes = dao.parseFromJSONToListOfObject(content, new Size());

			dao.updateByInputKey(product, Arrays.asList("product_id"));

			for (Size read : sizes) {
				read.setProduct_id(product.getProduct_id());
				if(read.getDisct_price()==null) {
					read.setDisct_price(0.0);
					read.setExpired_time(0);
				}
				if (dao.updateByInputKey(read, Arrays.asList("product_id", "size")) == 0) {
					read.setProduct_id(product.getProduct_id());
					read.setCreate_time(Integer.parseInt(Util.getCurrentDate()));
					dao.add(read);
				}
			}
			dao.updateByInputKey(thuoctinh, Arrays.asList("product_id"));
		}

		return "200";
	}

	@GET
	@Path("check/{productName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String check(@PathParam("productName") String name, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Product product = new Product();
		product.setName(name);
		if (dao.findByKey(product).isEmpty()) {
			return "false";
		} else {
			return "true";
		}
	}

	@GET
	@Path("add-cart/{productId}/{size}/{quantity}/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addCart(@PathParam("productId") String productId, @PathParam("size") String size,
			@PathParam("quantity") String quantity, @PathParam("token") String token,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Cart cart = new Cart();
		Product product = new Product();
		Claims claims = JWTUtil.decodeJWT(token);
		if (claims.getIssuer().equals("LienHoaCac")) {
			cart.setUser_id(Integer.parseInt(claims.getId()));
			product.setProduct_id(Integer.parseInt(productId));
			List<Product> produtcs = dao.findByKey(product);
			if (produtcs.isEmpty()) {
				return "product not found";
			} else {
				cart.setProduct_id(Integer.parseInt(productId));
				cart.setName(produtcs.get(0).getName());
				cart.setSize(size);
				cart.setPayment_id(0);
				List<Cart> list = dao.findByKey(cart);
				try {
					if (list.isEmpty()) {
						cart.setAmount(Integer.parseInt(quantity));
						dao.add(cart);
					} else {
						cart.setAmount(list.get(0).getAmount() + Integer.parseInt(quantity));
						dao.updateByInputKey(cart, Arrays.asList("user_id", "product_id", "size", "payment_id"));
					}

				} catch (Exception e) {
					return "203";
				}
			}

		} else {
			return "203";
		}

		return "200";
	}

	@GET
	@Path("check/size/{sizeCode}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String checkCodeSize(@PathParam("sizeCode") String code, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Size size = new Size();
		size.setCode(code);
		if (dao.findByKey(size).isEmpty()) {
			return "false";
		} else {
			return "true";
		}
	}

	@GET
	@Path("getProductByCode/{code}/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getByCode(@PathParam("code") String code,@PathParam("token") String token) {
		Size size = new Size();
		size.setCode(code);
		Gson gson = new Gson();
		Claims claims = JWTUtil.decodeJWT(token);
		if (claims.getIssuer().equals("LienHoaCac")) {
			List<Size> list = dao.findByKey(size); 
			if (!list.isEmpty()) {
				Cart cart = new Cart();
				cart.setCode(list.get(0).getCode());
				if (list.get(0).getExpired_time() >= Integer.parseInt(Util.getCurrentDate())) {
					cart.setDisct_price(list.get(0).getDisct_price());
				} else {
					cart.setDisct_price(0.0);
				}
				cart.setAmount(1);
				cart.setPrice(list.get(0).getPrice());
				cart.setProduct_id(list.get(0).getProduct_id());
				cart.setSize(list.get(0).getSize());
				Product product = new Product();
				product.setProduct_id(list.get(0).getProduct_id());
				List<Product> products = dao.findByKey(product);
				cart.setName(products.get(0).getName());
				return gson.toJson(cart);
			} else {
				return null;
			}
		} 
		return null;
	}
	
	@GET
	@Path("get-cart/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getCart(@PathParam("token") String token, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Cart cart = new Cart();
		cart.setPayment_id(0);
		Claims claims = JWTUtil.decodeJWT(token);
		JsonObject jsonObject = new JsonObject();
		if (claims.getIssuer().equals("LienHoaCac")) {
			cart.setUser_id(Integer.parseInt(claims.getId()));
			jsonObject.addProperty("message", "200");
			jsonObject.addProperty("itemInCart", dao.findByKey(cart).size());
			jsonObject.addProperty("userType", claims.getSubject());
			return jsonObject.toString();
		}
		jsonObject.addProperty("message", "203");
		return jsonObject.toString();
	}

	@GET
	@Path("get-cart-detail/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getCartDetail(@PathParam("token") String token,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Gson gson = new Gson();
		Cart cart = new Cart();
		cart.setPayment_id(0);
		Claims claims = JWTUtil.decodeJWT(token);
		JsonObject jsonObject = new JsonObject();
		if (claims.getIssuer().equals("LienHoaCac")) {
			return gson.toJson(dao.getCartBy(claims.getId()));
		}
		jsonObject.addProperty("message", "203");
		return null;
	}

	@PUT
	@Path("get-cart-detail-not-login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getCartDetailNotLogin(String cartInfo,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Cart cart = new Cart();
		cart.setPayment_id(0);
		Gson gson = new Gson();
		return gson.toJson(dao.getCartBy(cartInfo.split("\\|")));
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
	@Path("/get-list-hot-product")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getListHotProduct() {
		JSONObject object = new JSONObject();
		Gson gson = new Gson();
		List<CategoryScreen> list = dao.getListHotProducts();
		if(!list.isEmpty()){
			list = list.stream().map(item -> {
				item.setImage(getImagesUrl(item.getImage()).split(";")[0]);
				return item;
			}).collect(Collectors.toList());
			object.put("productOverview", list);
			return gson.toJson(list);
		}
		
		return "203";
	}
}
