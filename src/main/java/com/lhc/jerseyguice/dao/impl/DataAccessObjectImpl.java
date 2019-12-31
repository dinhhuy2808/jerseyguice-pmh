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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.cloud.sql.jdbc.Statement;
import com.lhc.jerseyguice.annotation.KeyColumn;
import com.lhc.jerseyguice.dao.Dao;
import com.lhc.jerseyguice.model.Product;
import com.lhc.jerseyguice.model.User;
import com.lhc.util.Util;

/**
 * A DataAccessObject has the responsibility to do all SQL for us.
 */
public class DataAccessObjectImpl<T extends Object> implements Dao {

	private static Connection connection = null;

	protected static PreparedStatement getPreparedStatement(String sql) {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ps;
	}

	protected static Connection getConnection() {

		if (connection == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/"+	"pmh"+"?serverTimezone=UTC&useLegacyDatetimeCode=false&useUnicode=true&characterEncoding=UTF-8",
						"root", "");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return connection;
	}

	@Override
	public List<?> findByKey(Object t) {
		StringBuilder sql = new StringBuilder("SELECT * FROM " + t.getClass().getSimpleName() + getSqlQueryForCondition(t));
		PreparedStatement ps = getPreparedStatement(sql.toString());
		List<T> results = new ArrayList<>();
		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				results.add(setToInstane(rs, t));
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
	@Override
	public List<?> findByGivenKey(Object t,String key) {
		StringBuilder sql = new StringBuilder("SELECT * FROM " + t.getClass().getSimpleName() + getSqlQueryForCondition(t,Arrays.asList(key.split(","))));
		PreparedStatement ps = getPreparedStatement(sql.toString());
		List<T> results = new ArrayList<>();
		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				results.add(setToInstane(rs, t));
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
	@Override
	public String addThenReturnId(Object t) {
		StringBuilder sql = new StringBuilder("INSERT INTO " + t.getClass().getSimpleName() + " ( " + getListOfFieldWithoutKey(t)
		+ " ) VALUES ( " + getListOfValueWithoutKey(t) + " )");
		PreparedStatement ps = getPreparedStatement(sql.toString());
		String key = "";
		try {
			ps.executeUpdate(sql.toString(),Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
			    key = rs.getString(1);
			    
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "none";
		} finally {

			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return key;
		}
	}
	@Override
	public boolean add(Object t) {
		StringBuilder sql = new StringBuilder("INSERT INTO " + t.getClass().getSimpleName() + " ( " + getListOfFieldWithoutKey(t)
		+ " ) VALUES ( " + getListOfValueWithoutKey(t) + " )");
		PreparedStatement ps = getPreparedStatement(sql.toString());
		try {
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {

			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}
	@Override
	public boolean deleteByKey(Object t) {
		StringBuilder sql = new StringBuilder("DELETE FROM " + t.getClass().getSimpleName() + " WHERE ");
		Class targetClass = t.getClass();

		PreparedStatement ps = getPreparedStatement(sql.toString() + String.join(" AND ", getKeyFieldsEqualValue(t)));
		try {
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {

			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}
	@Override
	public boolean updateByKey(Object t) {
		StringBuilder sql = new StringBuilder("UPDATE " + t.getClass().getSimpleName() + " SET "
				+ String.join(" , ", getNonKeyFieldsEqualValue(t)) + " WHERE "
				+ String.join(" , ", getKeyFieldsEqualValue(t)));
		Class targetClass = t.getClass();

		PreparedStatement ps = getPreparedStatement(sql.toString() + String.join(" AND ", getKeyFieldsEqualValue(t)));
		try {
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {

			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}
	
	/*return list of non-key-field with value*/ 
	private List<String> getNonKeyFieldsEqualValue(Object t) {

		List<String> keyFieldsEqualValue = Arrays.asList(t.getClass().getDeclaredFields()).stream()
				.filter(field -> !field.isAnnotationPresent(KeyColumn.class)).map(field -> {
					String fieldName = field.getName();
					String methodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase()
							+ fieldName.substring(1);
					String value = "";
					Method method;
					try {
						method = t.getClass().getDeclaredMethod(methodName);
						method.setAccessible(true);
						if (field.getType().getSimpleName().equals("String")) {
							value = "'" + method.invoke(t).toString() + "'";
						} else {
							value = method.invoke(t).toString();
						}
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						if (field.getType().getSimpleName().equals("String")) {
							return "`"+fieldName+"`" + " = ''"  ;
						} else {
							return "`"+fieldName+"`" + " = 0"  ;
						}
						
					}

					return "`"+fieldName+"`" + " = " + value;
				}).collect(Collectors.toList());
		return keyFieldsEqualValue;
	}

	/*return list of key-field with value*/ 
	private List<String> getKeyFieldsEqualValue(Object t) {

		List<String> keyFieldsEqualValue = Arrays.asList(t.getClass().getDeclaredFields()).stream()
				.filter(field -> field.isAnnotationPresent(KeyColumn.class)).map(field -> {
					String fieldName = field.getName();
					String methodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase()
							+ fieldName.substring(1);
					String value = "";
					Method method;
					try {
						method = t.getClass().getDeclaredMethod(methodName);
						method.setAccessible(true);
						if (field.getType().getSimpleName().equals("String")) {
							value = "'" + method.invoke(t).toString() + "'";
						} else {
							value = method.invoke(t).toString();
						}
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return "`"+fieldName+"`" + " = " + value;
				}).collect(Collectors.toList());
		return keyFieldsEqualValue;
	}

	/*return list of non-key-field*/
	private String getListOfFieldWithoutKey(Object t) {
		Class targetClass = t.getClass();
		StringBuilder sql = new StringBuilder();
		List<String> fields = Arrays.asList(targetClass.getDeclaredFields()).stream()
				.filter(field -> !field.isAnnotationPresent(KeyColumn.class)).map(field -> "`"+field.getName()+"`")
				.collect(Collectors.toList());
		return String.join(",", fields);
	}

	/*return list of field include non-key and key*/
	private String getListOfField(Object t) {
		Class targetClass = t.getClass();
		StringBuilder sql = new StringBuilder();
		List<String> fields = Arrays.asList(targetClass.getDeclaredFields()).stream().map(field -> field.getName())
				.collect(Collectors.toList());
		return String.join(",", fields);
	}

	/*return list of value include non-key and key*/
	private static String getListOfValue(Object t) {
		Class targetClass = t.getClass();
		StringBuilder sql = new StringBuilder();
		List<String> values = new ArrayList<>();
		for (Field field : targetClass.getDeclaredFields()) {
			String methodName = "get" + String.valueOf(field.getName().charAt(0)).toUpperCase()
					+ field.getName().substring(1);
			try {
				Method method = targetClass.getDeclaredMethod(methodName);
				method.setAccessible(true);
				if (method.invoke(t) != null) {
					if (field.getType().getSimpleName().equals("String")) {
						values.add("'" + method.invoke(t).toString() + "'");
					} else {
						values.add(method.invoke(t).toString());
					}

				} else {
					if (field.getType().getSimpleName().equals("String")) {
						values.add("''");
					} else {
						values.add("0");
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return String.join(",", values);
	}

	/*return list of non-key value*/
	private String getListOfValueWithoutKey(Object t) {
		Class targetClass = t.getClass();
		StringBuilder sql = new StringBuilder();
		List<String> values = new ArrayList<>();
		for (Field field : targetClass.getDeclaredFields()) {
			if (!field.isAnnotationPresent(KeyColumn.class)) {

				String methodName = "get" + String.valueOf(field.getName().charAt(0)).toUpperCase()
						+ field.getName().substring(1);
				try {
					Method method = targetClass.getDeclaredMethod(methodName);
					method.setAccessible(true);
					if (method.invoke(t) != null) {
						if (field.getType().getSimpleName().equals("String")) {
							values.add("'" + method.invoke(t).toString() + "'");
						} else {
							values.add(method.invoke(t).toString());
						}

					} else {
						if (field.getType().getSimpleName().equals("String")) {
							values.add("''");
						} else {
							values.add("0");
						}
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return String.join(",", values);
	}

	/*return string with field = value where value != null*/
	private static String getSqlQueryForCondition(Object t) {
		Class targetClass = t.getClass();
		StringBuilder sql = new StringBuilder();
		List<String> conditions = new ArrayList<String>();
		for (Field field : targetClass.getDeclaredFields()) {
			String methodName = "get" + String.valueOf(field.getName().charAt(0)).toUpperCase()
					+ field.getName().substring(1);
			try {
				Method method = targetClass.getDeclaredMethod(methodName);
				method.setAccessible(true);
				if (method.invoke(t) != null) {
					if (field.getType().getSimpleName().equals("String")) {
						conditions.add(field.getName() + " = " + "'" + method.invoke(t).toString() + "'");
					} else {
						conditions.add(field.getName() + " = " + method.invoke(t).toString());
					}

				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!conditions.isEmpty()) {
			sql.append(" WHERE ");
			sql.append(String.join(" AND ", conditions));
		}

		return sql.toString() + ";";
	}

	/*return string with field = value where value != null with given key*/
	private static String getSqlQueryForCondition(Object t, List<String> key) {
		Class targetClass = t.getClass();
		StringBuilder sql = new StringBuilder();
		List<String> conditions = new ArrayList<String>();
		for (Field field : targetClass.getDeclaredFields()) {
			if(key.contains(field.getName().toLowerCase())){
				String methodName = "get" + String.valueOf(field.getName().charAt(0)).toUpperCase()
						+ field.getName().substring(1);
				try {
					Method method = targetClass.getDeclaredMethod(methodName);
					method.setAccessible(true);
					if (method.invoke(t) != null) {
						if (field.getType().getSimpleName().equals("String")) {
							conditions.add("`"+field.getName()+"`" + " = " + "'" + method.invoke(t).toString() + "'");
						} else {
							conditions.add("`"+field.getName()+"`" + " = " + method.invoke(t).toString());
						}

					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		if (!conditions.isEmpty()) {
			sql.append(" WHERE ");
			sql.append(String.join(" AND ", conditions));
		}

		return sql.toString() + ";";
	}
	
	protected T setToInstane(ResultSet rs, Object t) {
		T newT = null;
		try {
			newT = (T) t.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Class targetClass = newT.getClass();

		for (Field field : targetClass.getDeclaredFields()) {
			String methodName = "set" + String.valueOf(field.getName().charAt(0)).toUpperCase()
					+ field.getName().substring(1);
			try {

				if (field.getType().getSimpleName().equals("Integer")) {
					Method method = targetClass.getDeclaredMethod(methodName, Integer.class);
					method.invoke(newT, rs.getInt(field.getName()));
				} else if (field.getType().getSimpleName().equals("Double")) {
					Method method = targetClass.getDeclaredMethod(methodName, Double.class);
					method.invoke(newT, rs.getDouble(field.getName()));
				} else {
					Method method = targetClass.getDeclaredMethod(methodName, String.class);
					method.invoke(newT, rs.getString(field.getName()));
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return newT;
	}

	@Override
	public T parseFromJSONToObject(String content, Object t) {
		JSONParser parser = new JSONParser();
		T newT = null;
		Object obj = null;
		try {
			obj = parser.parse(content);
			newT = (T) t.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONObject jsonObject = (JSONObject)obj;
		Class targetClass = newT.getClass();
		Map<String, String> map = new HashMap<String, String>();
		map = (Map<String, String>) jsonObject.get(targetClass.getSimpleName().toLowerCase());
		for (Field field : targetClass.getDeclaredFields()) {
			if(map.get(field.getName().toLowerCase())!=null) {
				String methodName = "set" + String.valueOf(field.getName().charAt(0)).toUpperCase()
						+ field.getName().substring(1);
				try {

					if (field.getType().getSimpleName().equals("Integer")) {
						Method method = targetClass.getDeclaredMethod(methodName, Integer.class);
						try {
							method.invoke(newT, Integer.parseInt(String.valueOf(map.get(field.getName().toLowerCase()))));
						} catch (NumberFormatException e) {
							method.invoke(newT, 0);
						}
						
					} else if (field.getType().getSimpleName().equals("Double")) {
						Method method = targetClass.getDeclaredMethod(methodName, Double.class);
						method.invoke(newT, Double.parseDouble(String.valueOf(map.get(field.getName().toLowerCase()))));
					} else {
						Method method = targetClass.getDeclaredMethod(methodName, String.class);
						method.invoke(newT, map.get(field.getName().toLowerCase()).toString());
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			

		}

		return newT;
	}
	
	@Override
	public List<?> parseFromJSONToListOfObject(String content, Object t) {
		List<T> results = new ArrayList<>();
		JSONParser parser = new JSONParser();
		T newT = null;
		Object obj = null;
		try {
			obj = parser.parse(content);
			newT = (T) t.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONObject jsonObject = (JSONObject)obj;
		Class targetClass = newT.getClass();
		JSONArray jsonArray = (JSONArray) jsonObject.get(targetClass.getSimpleName().toLowerCase());
		for(Object read : jsonArray) {
			Map<String, String> map = new HashMap<String, String>();
			map = (Map<String, String>) read;
			try {
				newT = (T) t.getClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(map.size()!=0) {
				for (Field field : targetClass.getDeclaredFields()) {
					
					if(map.get(field.getName().toLowerCase())!=null) {
						String methodName = "set" + String.valueOf(field.getName().charAt(0)).toUpperCase()
								+ field.getName().substring(1);
						try {

							if (field.getType().getSimpleName().equals("Integer")) {
								Method method = targetClass.getDeclaredMethod(methodName, Integer.class);
								method.invoke(newT, Integer.parseInt(String.valueOf(map.get(field.getName().toLowerCase()))));
							} else if (field.getType().getSimpleName().equals("Double")) {
								Method method = targetClass.getDeclaredMethod(methodName, Double.class);
								method.invoke(newT, Double.parseDouble(String.valueOf(map.get(field.getName().toLowerCase()))));
							} else {
								Method method = targetClass.getDeclaredMethod(methodName, String.class);
								method.invoke(newT, String.valueOf(map.get(field.getName().toLowerCase())));
							}
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					

				}
				results.add(newT);
			}
			
		}
		

		return results;
	}

	@Override
	public Integer updateByInputKey(Object t, List keys) {
		StringBuilder sql = new StringBuilder("UPDATE " + t.getClass().getSimpleName() + " SET "
				+ String.join(" , ", getNonKeyFieldsEqualValue(t))) ;
		List<String> newList = (List<String>) keys.stream().map(key -> String.valueOf(key).toLowerCase()).collect(Collectors.toList());
		sql.append(getSqlQueryForCondition(t, newList)) ;
		Class targetClass = t.getClass();
		int result = 0;
		PreparedStatement ps = getPreparedStatement(sql.toString());
		try {
			result = ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return result;
		} finally {

			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
	}


	public static void main(String[] args) {
		User user = new User();

		/*DataAccessObject<T> data = new DataAccessObject<>();
		System.out.println(data.getListOfField(user));
		List<User> users = (List<User>) data.findByKey(user);
		for (User read : users) {
			System.out.println(data.getListOfFieldWithoutKey(read));
			System.out.println(data.getListOfValueWithoutKey(read));
		}
		System.out.println(data.findByKey(user).size());*/
		user.setAddress("avc");
		user.setFirstname("Nam");
		List<String> list= Arrays.asList("address","firstname");
		System.out.println(getSqlQueryForCondition(user,list));
	}

	@Override
	public List findByKeySortBy(Object t, String sort) {

		StringBuilder sql = new StringBuilder("SELECT * FROM " + t.getClass().getSimpleName() + getSqlQueryForCondition(t) + " ORDER BY " + sort);
		PreparedStatement ps = getPreparedStatement(sql.toString());
		List<T> results = new ArrayList<>();
		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				results.add(setToInstane(rs, t));
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

	@Override
	public List findByGivenKeySortBy(Object t, String key, String sort) {
		StringBuilder sql = new StringBuilder("SELECT * FROM " + t.getClass().getSimpleName() + getSqlQueryForCondition(t,Arrays.asList(key.split(","))) + " ORDER BY " + sort);
		PreparedStatement ps = getPreparedStatement(sql.toString());
		List<T> results = new ArrayList<>();
		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				results.add(setToInstane(rs, t));
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