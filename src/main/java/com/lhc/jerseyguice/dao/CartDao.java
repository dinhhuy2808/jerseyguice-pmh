package com.lhc.jerseyguice.dao;

import java.util.List;

import com.lhc.jerseyguice.model.Cart;

public interface CartDao<T> extends Dao<T> {
	void updatePayment(Cart cart,int paymentId);
}
