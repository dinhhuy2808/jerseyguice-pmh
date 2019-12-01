package com.lhc.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.lhc.jerseyguice.dao.TreeFolderDao;
import com.lhc.jerseyguice.dao.impl.TreeFolderDaoImpl;


public class CreateModel {

	static String template = "package com.lhc.jerseyguice.model;\n\n" + "import lombok.AllArgsConstructor;\n"
			+ "import lombok.NoArgsConstructor;\n" + "import lombok.Setter;\n" + "import lombok.Getter;\n\n"
			+ "@Getter @Setter @AllArgsConstructor @NoArgsConstructor\n" + "public class ModelNameTemplate {\n"
			+ "template\n" + "}      \n";
	static String templateAngular = "export class ModelNameTemplate { \n" + "template\n"
			+ "   constructor(constructorArgs) {\n" + "constructortemplate\n" + "   }\n" + "}";
	static String templateAngularNoCons = "export class ModelNameTemplate { \n" + "template\n"
			+ "  \n" + "}";
	static String templateDAO="package com.lhc.jerseyguice.dao;\r\n" + 
			"\r\n" + 
			"import com.lhc.jerseyguice.model.TableNameTemplate;\r\n" + 
			"\r\n" + 
			"public interface TableNameTemplateDao<T> extends Dao<T> {\r\n" + 
			"}\r\n" + 
			"";
	static String templateDAOImpl = "package com.lhc.jerseyguice.dao.impl;\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"import com.lhc.jerseyguice.dao.TableNameTemplateDao;\r\n" + 
			"import com.lhc.jerseyguice.model.TableNameTemplate;\r\n" + 
			"\r\n" + 
			"public class TableNameTemplateDaoImpl extends DataAccessObjectImpl<TableNameTemplate> implements TableNameTemplateDao {\r\n" + 
			"	\r\n" + 
			"}";

	public static void main(String[] args) throws IOException {
		// createJavaModel();
//		createAngularModel();
//		createDaoNImplFile();
//		createScreenVarsFile();
		/*int i = 0 ;
		i++;
		System.out.println(i);
		System.out.println(i+1);
		System.out.println(i);*/
		for(int i = 1;i< 1002;i++){
			System.out.println(i);
		}
	}

	public static void createJavaModel() throws IOException {

		String[] tables;
		String url = "F:/workspace27092019/jerseyguice/src/main/java/com/lhc/jerseyguice/model/";
		String text = new String(Files.readAllBytes(Paths.get("create-db.sql")), StandardCharsets.UTF_8);
		tables = text.split("CREATE TABLE");
		for (String read : tables) {
			System.out.println("-----------------");
			System.out.println(read.trim());
			if (!read.trim().equals("")) {
				String modelName = read.substring(0, read.indexOf("(")).replaceAll("`", "").trim();
				modelName = String.valueOf(modelName.charAt(0)).toUpperCase() + modelName.substring(1);
				String[] fields = read.substring(read.indexOf("(") + 1, read.lastIndexOf(")")).split(",");
				String fieldsContent = "";
				for (String field : fields) {
					if (String.valueOf(field.trim().charAt(0)).equals("`")) {
						String fieldName = field.trim().split(" ")[0].trim().replaceAll("`", "");
						String fieldType = field.trim().split(" ")[1].trim();
						if (fieldType.indexOf("(") > 0) {
							fieldType = fieldType.substring(0, fieldType.indexOf("("));
						}
						if (fieldType.equals("int")) {
							fieldType = "Integer";
						} else if (fieldType.equals("double")) {
							fieldType = "Double";
						} else {
							fieldType = "String";
						}

						fieldsContent += "/tprivate " + fieldType + " " + fieldName + ";/n";

					}
				}

				writeFile(template.replace("template", fieldsContent).replace("ModelNameTemplate", modelName),
						url + modelName + ".java");
			}
		}

	}

	public static void createAngularModel() throws IOException {

		String[] tables;
		String url = "F:/workspace27092019/lhc/src/app/shared/models/";
		String text = new String(Files.readAllBytes(Paths.get("create-db.sql")), StandardCharsets.UTF_8);
		tables = text.split("CREATE TABLE");
		for (String read : tables) {
			System.out.println("-----------------");
			System.out.println(read.trim());
			if (!read.trim().equals("")) {
				String modelName = read.substring(0, read.indexOf("(")).replaceAll("`", "").trim();
				String Name = String.valueOf(modelName.charAt(0)).toUpperCase() + modelName.substring(1);
				String[] fields = read.substring(read.indexOf("(") + 1, read.lastIndexOf(")")).split(",");
				String fieldsContent = "";
				String fieldsContentConstr = "";
				String constructorArgs = "";
				for (String field : fields) {
					if (String.valueOf(field.trim().charAt(0)).equals("`")) {
						String fieldName = field.trim().split(" ")[0].trim().replaceAll("`", "");
						String fieldType = field.trim().split(" ")[1].trim();
						if (fieldType.indexOf("(") > 0) {
							fieldType = fieldType.substring(0, fieldType.indexOf("("));
						}
						if (fieldType.equals("int")) {
							fieldType = "number";
						} else if (fieldType.equals("double")) {
							fieldType = "number";
						} else {
							fieldType = "string";
						}

						fieldsContent += "\t" + fieldName + ":" + fieldType + ";\n";
						constructorArgs += fieldName + ":" + fieldType + ",";
						fieldsContentConstr += "\t\t" + "this." + fieldName + " = " + fieldName + ";\n";
					}
				}

				writeFile(templateAngular
						.replace("constructorArgs", constructorArgs.substring(0, constructorArgs.lastIndexOf(",")))
						.replace("constructortemplate", fieldsContentConstr).replace("template", fieldsContent)
						.replace("ModelNameTemplate", Name), url + modelName + ".ts");
			}
		}

	}

	public static void writeFile(String content, String location) {
		String fileData = content;
		try {
			FileOutputStream fos = new FileOutputStream(location);
			fos.write(fileData.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createDaoNImplFile() throws IOException {

		String[] tables;
		String urlDAO = "F:\\workspace27092019\\jerseyguice\\src\\main\\java\\com\\lhc\\jerseyguice\\dao\\";
		String urlDAOImpl = "F:\\workspace27092019\\jerseyguice\\src\\main\\java\\com\\lhc\\jerseyguice\\dao\\impl\\";
		String text = new String(Files.readAllBytes(Paths.get("create-db.sql")), StandardCharsets.UTF_8);
		tables = text.split("CREATE TABLE");
		for (String read : tables) {
			if(!read.trim().equals("")) {
				
					String modelName = read.substring(0, read.indexOf("(")).replaceAll("`", "").trim();
					String Name = String.valueOf(modelName.charAt(0)).toUpperCase() + modelName.substring(1);
				/*
				 * String daoContent = templateDAO.replace("TableNameTemplate", Name); String
				 * daoImplContent = templateDAOImpl.replace("TableNameTemplate", Name);
				 * writeFile(daoContent, urlDAO + Name + "Dao.java"); writeFile(daoImplContent,
				 * urlDAOImpl + Name + "DaoImpl.java");
				 */
					System.out.println("import com.lhc.jerseyguice.dao."+Name+"Dao;");
					System.out.println("import com.lhc.jerseyguice.dao.impl."+Name+"DaoImpl;");
			}
			
		}

	}
	
	public static void createScreenVarsFile() throws IOException {

		String[] tables;
		String url = "C:\\lhc-angular\\lhc\\src\\app\\shared\\screenvars\\";
		String text = new String(Files.readAllBytes(Paths.get("createScreenVars.txt")), StandardCharsets.UTF_8);
		tables = text.split("---------------------------");
		for (String read : tables) {
			System.out.println("-----------------");
			System.out.println(read.trim());
			if (!read.trim().equals("")) {
				String modelName = read.split(":")[0].trim();
				String[] fields = read.split(":")[1].split(";");
				String fieldsContent = "";
				for (String field : fields) {
					if(!field.trim().equals("")){
						System.out.println(field);
						fieldsContent += "\t " + field.split(" ")[0].trim() + " :" + (field.split(" ")[1].trim().equals("Integer")?"number":field.split(" ")[1].trim()) + ";\n";
					}
					
				}

				writeFile(templateAngularNoCons.replace("template", fieldsContent).replace("ModelNameTemplate", modelName),
						url + modelName + ".ts");
			}
		}

	}
}
