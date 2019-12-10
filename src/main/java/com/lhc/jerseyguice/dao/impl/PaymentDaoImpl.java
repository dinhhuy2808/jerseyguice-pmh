package com.lhc.jerseyguice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lhc.jerseyguice.dao.PaymentDao;
import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.model.Payment;
import com.lhc.jerseyguice.screenvars.CategoryScreen;
import com.lhc.jerseyguice.screenvars.PaymentScreen;
import com.lhc.util.Util;

public class PaymentDaoImpl extends DataAccessObjectImpl<Payment> implements PaymentDao {
	public PaymentScreen getPaymentDetailForCheckOut(int userId) {
		StringBuilder sql = new StringBuilder("");
		sql.append(
				" select c.user_id,c.product_id,c.amount,c.payment_id,c.create_time,c.name,c.size,(select code from size where product_id = c.product_id and size = c.size) as code , u.firstname, u.address,u.phone,  ");
		sql.append(" (select price from size where product_id = c.product_id and size = c.size) as price, ");
		sql.append(" (select disct_price from size where product_id = c.product_id and size = c.size ");
		sql.append(" and expired_time <= ?) as disct_price ,");
		sql.append(" (select max(title) from payment) as title ");
		sql.append(" from cart c join user u on c.user_id = u.user_id ");
		sql.append(" where c.user_id = ? and payment_id = 0  ");
		PreparedStatement ps = null;
		PaymentScreen paymentScreen = new PaymentScreen();
		int total = 0;
		try {
			ps = getConnection().prepareStatement(sql.toString());
			ps.setString(1, Util.getCurrentDate());
			ps.setInt(2,userId);
			ResultSet rs = ps.executeQuery();
			
			List<Cart> carts = new ArrayList<>();
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
				cart.setUser_id(userId);
				carts.add(cart);
				
				if(cart.getDisct_price()!= 0 && cart.getPrice() >= cart.getDisct_price()){
					total += cart.getAmount()*cart.getDisct_price();
				} else {
					total += cart.getAmount()*cart.getPrice();
				}
				paymentScreen.setAddress(rs.getString("address"));
				paymentScreen.setName(rs.getString("firstname"));
				paymentScreen.setPayment_id(0);
				paymentScreen.setPhone(rs.getString("phone"));
				paymentScreen.setUserId(userId);
				paymentScreen.setTitle(rs.getString("title"));
			}
			paymentScreen.setTotal(total);
			paymentScreen.setCarts(carts);
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
		return paymentScreen;
	}

	@Override
	public PaymentScreen getPaymentDetailForCheckOutNotLogin(String[] cartDetail) {
		StringBuilder sql = new StringBuilder("");

		sql.append(" select c.product_id,c.create_time,p.name,c.size,c.code,c.price, ");
		sql.append(" (select disct_price from size where product_id = c.product_id and size = c.size ");
		sql.append(" and expired_time <= ?) as disct_price, ");
		sql.append(" (select max(title) from payment) as title ");
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
		List<Cart> carts = new ArrayList<>();
		int total = 0;
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
				
				carts.add(cart);
				
				if(cart.getDisct_price()!= 0 && cart.getPrice() >= cart.getDisct_price()){
					total += cart.getAmount()*cart.getDisct_price();
				} else {
					total += cart.getAmount()*cart.getPrice();
				}
				paymentScreen.setAddress("");
				paymentScreen.setName("");
				paymentScreen.setPayment_id(0);
				paymentScreen.setPhone("");
				paymentScreen.setTitle(rs.getString("title"));
			}
			paymentScreen.setTotal(total);
			paymentScreen.setCarts(carts);
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
		return paymentScreen;
	}

}
