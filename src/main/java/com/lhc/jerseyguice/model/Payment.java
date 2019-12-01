package com.lhc.jerseyguice.model;

import com.lhc.jerseyguice.annotation.KeyColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

public class Payment {
	@KeyColumn
	private Integer payment_id;
	private Integer user_id;
	private Double sum;
	private Integer status_id;
	private Integer create_time;
	private String title;
	private String pay_type;
	private Double promotion;
	private Double total;
	private String seen_flag;
	private String ship;
	private String address;
	private String voucher;
	private Integer shipfee;
	private String shipcode;
	private String note;
	private String phone;
	private String name;
	private String tinhthanh;
	private String quanhuyen;
	private String hinhthuc;
	public Integer getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public Integer getStatus_id() {
		return status_id;
	}
	public void setStatus_id(Integer status_id) {
		this.status_id = status_id;
	}
	public Integer getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public Double getPromotion() {
		return promotion;
	}
	public void setPromotion(Double promotion) {
		this.promotion = promotion;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getSeen_flag() {
		return seen_flag;
	}
	public void setSeen_flag(String seen_flag) {
		this.seen_flag = seen_flag;
	}
	public String getShip() {
		return ship;
	}
	public void setShip(String ship) {
		this.ship = ship;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public Integer getShipfee() {
		return shipfee;
	}
	public void setShipfee(Integer shipfee) {
		this.shipfee = shipfee;
	}
	public String getShipcode() {
		return shipcode;
	}
	public void setShipcode(String shipcode) {
		this.shipcode = shipcode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
