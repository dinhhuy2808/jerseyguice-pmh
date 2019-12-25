package com.lhc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;

public class Util {

	public static String toJSONString(Map<String, Object> T) {
		JsonObject jsonObject = new JsonObject();
		Gson gsonBuilder = new GsonBuilder().create();
		JsonParser parser = new JsonParser();
		for (String key : T.keySet()) {
			JsonElement element = parser.parse(gsonBuilder.toJson(T.get(key)));
			jsonObject.add(key, element);
		}
		return jsonObject.toString();
	}

	public static String getCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	public static Properties getDbProperties() {
		 Properties prop = new Properties();
		 try (InputStream input = new FileInputStream(System.getProperties().get("user.dir")+"/"+"src/main/java/com/lhc/jerseyguice/resource/db.properties")) {

	            // load a properties file
	            prop.load(input);

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		return prop;
	}

	public static void main(String[] args) throws IOException {
	}
}
