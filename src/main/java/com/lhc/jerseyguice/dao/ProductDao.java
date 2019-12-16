package com.lhc.jerseyguice.dao;

import java.util.List;
import java.util.Map;

import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.model.Product;
import com.lhc.jerseyguice.screenvars.CategoryScreen;

public interface ProductDao<T> extends Dao<T> {
	List<CategoryScreen> getProductByCategoryName(String catName, String page);
	Map<String,List<Cart>> getCartBy(String userId);
	Map<String,List<Cart>> getCartBy(String[] cardDetail);
	List<CategoryScreen> search(String keyword, String page);
}
