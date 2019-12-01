package com.lhc.jerseyguice.dao.impl;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lhc.jerseyguice.dao.CartDao;
import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.screenvars.PaymentScreen;
import com.lhc.util.Util;

public class CartDaoImpl extends DataAccessObjectImpl<Cart> implements CartDao {

	@Override
	public void updatePayment(Cart cart, int paymentId) {
		StringBuilder sql = new StringBuilder("update cart set payment_id = ?, disct_price = ?, price = ? "
				+ ", amount = ? where payment_id = 0 and user_id = ? "
				+ "and product_id = ?");
		
		PreparedStatement ps = null;
		int total = 0;
		try {
			ps = getConnection().prepareStatement(sql.toString());
			ps.setInt(1, paymentId);
			ps.setDouble(2, cart.getDisct_price());
			ps.setDouble(3, cart.getPrice());
			ps.setInt(4, cart.getAmount());
			ps.setInt(5, cart.getUser_id());
			ps.setInt(6, cart.getProduct_id());
			int result = ps.executeUpdate();
			
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
	}
	
}