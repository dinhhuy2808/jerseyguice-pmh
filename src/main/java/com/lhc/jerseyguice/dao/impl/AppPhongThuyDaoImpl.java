package com.lhc.jerseyguice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lhc.jerseyguice.dao.AppPhongThuyDao;
import com.lhc.jerseyguice.model.AppPhongThuyRespone;

public class AppPhongThuyDaoImpl extends DataAccessObjectImpl<AppPhongThuyRespone> implements AppPhongThuyDao {

	@Override
	public AppPhongThuyRespone runApp(int yearOfBirth) {
		String  sql = "SELECT"
				+ " can.CAN as can,"
				+ " chi.CHI as chi,"
				+ " mang.id as id "
				+ "FROM can, chi, mang WHERE can.a= ? "
				+ "AND chi.b=? AND mang.id = can.AC + chi.BC;";
		AppPhongThuyRespone result = new AppPhongThuyRespone();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(sql);
			ps.setInt(1, yearOfBirth % 10);
			ps.setInt(2, yearOfBirth % 12);
			rs = ps.executeQuery();
			while (rs.next()){
				result.setCan(rs.getString("can"));
				result.setChi(rs.getString("chi"));
				result.setId(rs.getString("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				disconnect();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

}
