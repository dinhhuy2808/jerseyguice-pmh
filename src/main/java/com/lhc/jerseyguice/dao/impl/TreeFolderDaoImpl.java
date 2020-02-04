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

package com.lhc.jerseyguice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lhc.jerseyguice.dao.TreeFolderDao;
import com.lhc.jerseyguice.model.Treefolder;

public class TreeFolderDaoImpl extends DataAccessObjectImpl<Treefolder> implements TreeFolderDao {
	@Override
	public Map<String, List<String>> getTreefolderDetail(){
		String  sql = "select \n" +
		        "GROUP_CONCAT(c.cat_id SEPARATOR '; ') as cat_ids,\n" +
		        "GROUP_CONCAT(c.folder_id SEPARATOR '; ') as folder_ids,\n" +
		        "GROUP_CONCAT(c.cat_name SEPARATOR '; ') as cat_names,\n" +
		        "(select folder_name from treefolder t where t.folder_id = c.folder_id ) as folder_name," +
		        "(select t.index from treefolder t where t.folder_id = c.folder_id ) as folder_index from category  c group by folder_name order by folder_index asc;\n";
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				map.put(rs.getString("folder_name"), Arrays.asList(rs.getString("cat_names").split(";")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				disconnect();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}
	
	@Override
	public void updateCatName(String treeName, String catName, String newCatName){
		String sql = "UPDATE CATEGORY SET CAT_NAME = ? WHERE CAT_NAME = ? AND FOLDER_ID = (SELECT FOLDER_ID FROM TREEFOLDER WHERE FOLDER_NAME = ?)";
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql);
			ps.setString(1, newCatName.trim());
			ps.setString(2, catName.trim());
			ps.setString(3, treeName.trim());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				disconnect();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}