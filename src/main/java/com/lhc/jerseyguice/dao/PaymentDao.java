package com.lhc.jerseyguice.dao;

import com.lhc.jerseyguice.model.Payment;
import com.lhc.jerseyguice.screenvars.PaymentScreen;

public interface PaymentDao<T> extends Dao<T> {
	PaymentScreen getPaymentDetailForCheckOut (int userId);
}
