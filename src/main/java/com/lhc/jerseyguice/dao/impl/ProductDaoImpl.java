package com.lhc.jerseyguice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lhc.jerseyguice.dao.ProductDao;
import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.model.Product;
import com.lhc.jerseyguice.screenvars.CategoryScreen;
import com.lhc.jerseyguice.screenvars.PaymentScreen;
import com.lhc.util.Util;

public class ProductDaoImpl extends DataAccessObjectImpl<Product> implements ProductDao {

	@Override
	public List getProductByCategoryName(String catName, String page) {
		StringBuilder sql = new StringBuilder("select p.name, p.image,p.cat_id,p.create_time,p.product_id, ");
		sql.append("(select MIN(price) from size where product_id = p.product_id) as price, ");
		sql.append("(select disct_price from size where product_id = p.product_id ");
		sql.append("and price =  (select MIN(price) from size where product_id = p.product_id) ");
		sql.append("and expired_time <= ?) as discount ");
		sql.append("from product p ");
		sql.append("where p.cat_id = (select cat_id from category where cat_name = ?) LIMIT 12 OFFSET ? ");
		List<CategoryScreen> list = new ArrayList<CategoryScreen>();
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql.toString());
			ps.setString(1, Util.getCurrentDate());
			ps.setString(2, catName.replace("-", " "));
			ps.setInt(3, (Integer.parseInt(page) - 1) * 12);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryScreen catScreen = new CategoryScreen();
				catScreen.setCatId(rs.getInt("cat_id"));
				catScreen.setName(rs.getString("name"));
				catScreen.setProductId(rs.getInt("product_id"));
				catScreen.setPrice(rs.getString("price"));
				catScreen.setDiscountPrice(rs.getString("discount"));
				if ((Integer.parseInt(Util.getCurrentDate()) - Integer.parseInt(rs.getString("create_time"))) < 100) {
					catScreen.setNew(true);
				} else {
					catScreen.setNew(false);
				}
				catScreen.setImage(rs.getString("image"));
				list.add(catScreen);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public Map getCartBy(String userId) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select c.user_id,c.product_id,c.amount,c.payment_id,c.create_time,c.name,c.size,c.code,  ");
		sql.append(" (select price from size where product_id = c.product_id and size = c.size) as price, ");
		sql.append(" (select disct_price from size where product_id = c.product_id and size = c.size ");
		sql.append(" and expired_time <= ?) as disct_price ");
		sql.append(" from cart c ");
		sql.append(" where c.user_id = ? and payment_id = 0 order by c.name ");
		PreparedStatement ps = null;
		PaymentScreen paymentScreen = new PaymentScreen();
		Map<String, List<Cart>> map = new HashMap<String, List<Cart>>();
		try {
			ps = getConnection().prepareStatement(sql.toString());
			ps.setString(1, Util.getCurrentDate());
			ps.setString(2, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Cart cart = new Cart();
				cart.setAmount(rs.getInt("amount"));
				cart.setCode(rs.getString("code"));
				cart.setCreate_time(rs.getInt("create_time"));
				cart.setDisct_price(rs.getDouble("disct_price"));
				cart.setName(rs.getString("name"));
				cart.setPayment_id(0);
				cart.setPrice(rs.getDouble("price"));
				cart.setProduct_id(rs.getInt("product_id"));
				cart.setSize(rs.getString("size"));
				cart.setUser_id(Integer.parseInt(userId));
				List<Cart> carts = new ArrayList<>();
				if (map.containsKey(rs.getString("name").trim())) {
					carts = map.get(rs.getString("name").trim());
				}
				carts.add(cart);
				map.put(rs.getString("name").trim(), carts);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public Map getCartBy(String[] cartDetail) {
		StringBuilder sql = new StringBuilder("");

		sql.append(" select c.product_id,c.create_time,p.name,c.size,c.code,c.price, ");
		sql.append(" (select disct_price from size where product_id = c.product_id and size = c.size ");
		sql.append(" and expired_time <= ?) as disct_price ");
		sql.append(" from size c left join product p on c.product_id = p.product_id ");
		sql.append(" where ");
		List<String> conditions = new ArrayList<>();
		for (String read: cartDetail) {
			conditions.add("(c.product_id = "+read.split(";")[0]+ " and c.size = '" +read.split(";")[1]+"')");
		}
		sql.append(String.join(" or ", conditions));
		sql.append(" order by c.product_id");
		PreparedStatement ps = null;
		PaymentScreen paymentScreen = new PaymentScreen();
		Map<String, List<Cart>> map = new HashMap<String, List<Cart>>();
		try {
			ps = getConnection().prepareStatement(sql.toString());
			ps.setString(1, Util.getCurrentDate());
			// ps.setString(2,userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Cart cart = new Cart();
				
				cart.setCode(rs.getString("code"));
				cart.setCreate_time(rs.getInt("create_time"));
				cart.setDisct_price(rs.getDouble("disct_price"));
				cart.setName(rs.getString("name"));
				cart.setPayment_id(0);
				cart.setPrice(rs.getDouble("price"));
				cart.setProduct_id(rs.getInt("product_id"));
				cart.setSize(rs.getString("size"));
				for (String read : cartDetail) {
					if (read.contains(cart.getProduct_id()+";"+cart.getSize())){
						cart.setAmount(Integer.parseInt(read.split(";")[2]));
						break;
					}
				}
				
				List<Cart> carts = new ArrayList<>();
				if (map.containsKey(rs.getString("name").trim())) {
					carts = map.get(rs.getString("name").trim());
				}
				carts.add(cart);
				map.put(rs.getString("name").trim(), carts);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public List search(String keyword, String page) {
		StringBuilder sql = new StringBuilder("select p.name, p.image,p.cat_id,p.create_time,p.product_id, ");
		sql.append("(select MIN(price) from size where product_id = p.product_id) as price, ");
		sql.append("(select disct_price from size where product_id = p.product_id ");
		sql.append("and price =  (select MIN(price) from size where product_id = p.product_id) ");
		sql.append("and expired_time <= ?) as discount ");
		sql.append("from product p ");
		sql.append("where p.product_id in (select product_id from thuoctinh where menh like '%"+keyword+"%') LIMIT 12 OFFSET ? ");
		List<CategoryScreen> list = new ArrayList<CategoryScreen>();
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql.toString());
			ps.setString(1, Util.getCurrentDate());
			ps.setInt(2, (Integer.parseInt(page) - 1) * 12);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryScreen catScreen = new CategoryScreen();
				catScreen.setCatId(rs.getInt("cat_id"));
				catScreen.setName(rs.getString("name"));
				catScreen.setProductId(rs.getInt("product_id"));
				catScreen.setPrice(rs.getString("price"));
				catScreen.setDiscountPrice(rs.getString("discount"));
				if ((Integer.parseInt(Util.getCurrentDate()) - Integer.parseInt(rs.getString("create_time"))) < 100) {
					catScreen.setNew(true);
				} else {
					catScreen.setNew(false);
				}
				catScreen.setImage(rs.getString("image"));
				list.add(catScreen);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	@Override
	public List<CategoryScreen> getListHotProducts() {
		StringBuilder sql = new StringBuilder("select p.name, p.image,p.cat_id,p.create_time,p.product_id, ");
		sql.append("(select MIN(price) from size where product_id = p.product_id) as price, ");
		sql.append("(select disct_price from size where product_id = p.product_id ");
		sql.append("and price =  (select MIN(price) from size where product_id = p.product_id) ");
		sql.append("and expired_time <= ?) as discount ");
		sql.append("from product p where p.image <> '' ");
		sql.append("order by p.create_time desc LIMIT 12");
		List<CategoryScreen> list = new ArrayList<CategoryScreen>();
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql.toString());
			ps.setString(1, Util.getCurrentDate());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryScreen product = new CategoryScreen();
				product.setCatId(rs.getInt("cat_id"));
				product.setName(rs.getString("name"));
				product.setProductId(rs.getInt("product_id"));
				product.setPrice(rs.getString("price"));
				product.setDiscountPrice(rs.getString("discount"));
				if ((Integer.parseInt(Util.getCurrentDate()) - Integer.parseInt(rs.getString("create_time"))) < 100) {
					product.setNew(true);
				} else {
					product.setNew(false);
				}
				product.setImage(rs.getString("image"));
				list.add(product);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	@Override
	public List getAllProduct() {

		StringBuilder sql = new StringBuilder("select * from product where product_id in (select distinct product_id from size)");
		PreparedStatement ps = getPreparedStatement(sql.toString());
		List<Product> results = new ArrayList<>();
		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				results.add(setToInstane(rs, new Product()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return results;
	
	}
}