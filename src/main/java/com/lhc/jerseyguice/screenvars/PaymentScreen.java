package com.lhc.jerseyguice.screenvars;

import java.util.List;

import com.lhc.jerseyguice.model.Cart;
import com.lhc.jerseyguice.model.Settingshop;
import com.lhc.jerseyguice.model.Size;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

public class PaymentScreen {
	private Integer payment_id;
	private Integer userId;
	private String status;
	private String voucher;
	private String note;
	private String ship;
	private String type;
	private String name;
	private String phone;
	private String address;
	private List<Cart> carts;
	private Settingshop settingShop;
	private String title;
	private Double shipfee;
	private Integer total;
	private String tinhthanh;
	private String quanhuyen;
	private String hinhthuc;
	public Integer getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getShip() {
		return ship;
	}
	public void setShip(String ship) {
		this.ship = ship;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Settingshop getSettingShop() {
		return settingShop;
	}
	public void setSettingShop(Settingshop settingShop) {
		this.settingShop = settingShop;
	}
	public List<Cart> getCarts() {
		return carts;
	}
	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getShipfee() {
		return shipfee;
	}
	public void setShipfee(Double shipfee) {
		this.shipfee = shipfee;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getTinhthanh() {
		return tinhthanh;
	}
	public void setTinhthanh(String tinhthanh) {
		this.tinhthanh = tinhthanh;
	}
	public String getQuanhuyen() {
		return quanhuyen;
	}
	public void setQuanhuyen(String quanhuyen) {
		this.quanhuyen = quanhuyen;
	}
	public String getHinhthuc() {
		return hinhthuc;
	}
	public void setHinhthuc(String hinhthuc) {
		this.hinhthuc = hinhthuc;
	}
	
}      
