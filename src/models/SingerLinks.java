package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SingerLinks {
	public static int countFile = 1;
	public static int countEntity = 1;
	public static String singerName = "";
	
	public static boolean check(String string) {
		try {
			FileInputStream fis = new FileInputStream(new File("specialCharacters.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			while (br.ready()) {
				String line = br.readLine();
				if (string.contains(line)) {
					br.close();
					return false;
				}
			}
			
			char array[] = string.toCharArray();
			for (char element: array) {
				if (element == '\'') {
					br.close();
					return false;
				}
			}
			
			br.close();
			isr.close();
			fis.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean checkSinger(String name) {
		try {
			FileInputStream fis = new FileInputStream(new File("database/singerDB/singer_list.txt"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			while (br.ready()) {
				String line = br.readLine();
				if (line.toLowerCase().equals(name.toLowerCase())) {
					br.close();
					isr.close();
					fis.close();
					return true;
				}
			}
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static String information(String html, String path) {
		try {
			
			Writer writer = new FileWriter(new File(path), true);
			Writer writer1 = new FileWriter(new File("database/singerDB/singer_list.txt"),true);
			
			Document doc = Jsoup.parse(html);
			
			Elements singerNameClass = doc.getElementsByClass("info-summary");
			
			
			parse1:
			for (Element name: singerNameClass) {
				for (Element line: name.getElementsByTag("h1")) {
					singerName = line.text();
					break parse1;
				}
			}
			
			if (!check(singerName)) {
				writer.close();
				writer1.close();
				return "";
			} else {
				if (checkSinger(singerName)) {
					writer.close();
					writer1.close();
					return "";
				}
				char array[] = singerName.toCharArray();
				if (array[array.length-1] == ' ') {
					writer.close();
					writer1.close();
					return "";
				}
			}
			
			writer1.append(singerName);
			writer1.append("\n");
			
			singerName = singerName.replaceAll(" ", "_");
			Elements basicInformationClass = doc.getElementsByClass("hoz-list");
			for (Element basicInformationTag : basicInformationClass) {
				for (Element line : basicInformationTag.getElementsByTag("li")) {
					if (line.text().startsWith("Tên thật")) {
						writer.append("group1:" + singerName + " group1:birthName " + "\"" + cutName(line.text()) + "\" .");
						writer.append("\n");
					}

					if (line.text().startsWith("Ngày sinh")) {
						writer.append("group1:" + singerName + " group1:birthDate " + "\""
								+ convertBirthDay(cutBirthDay(line.text())) + "\" .");
						writer.append("\n");
					}
				}
			}

			Elements imgClass = doc.getElementsByClass("inside");

			for (Element imgTag : imgClass) {
				for (Element line : imgTag.getElementsByTag("img")) {
					writer.append("group1:" + singerName + " group1:image " + "\"" + line.attr("src") + "\" .");
					writer.append("\n");
				}
			}
			
			writer.close();
			writer1.close();
			return singerName;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	public static String cutName(String name) {
		if (name.length() < 11) {
			return "";
		}
		if (name.contains("\"")) {
			return "";
		}
		return name.substring(10);
	}

	public static String cutBirthDay(String birthday) {
		if (birthday.length() < 12) {
			return "yyyy/mm/dd";
		}
		return birthday.substring(11);
	}
	
	public static String convertBirthDay(String birthday) {
		if (!birthday.equals("yyyy/mm/dd")) {
			if (birthday.length() == 3) {
				String data[] = birthday.split("(/|\\.)");
				return "yyyy/" + "0" + data[1] + "/" + "0" + data[0];
			} else if (birthday.length() == 4) {
				return birthday + "/mm/dd";
			} else if (birthday.length() == 5) {
				String data[] = birthday.split("(/|\\.)");
				return "yyyy/" + data[1] + "/" + data[0];
			} else if (birthday.length() == 6) {
				String data[] = birthday.split("(/|\\.)");
				return data[1] + "/" + "0" + data[0] + "/dd";
			} else if (birthday.length() == 7) {
				String data[] = birthday.split("(/|\\.)");
				return data[1] + "/" + data[0] + "/dd";
			} else if (birthday.length() == 8) {
				String data[] = birthday.split("(/|\\.)");
				return data[2] + "/" + "0" + data[1] + "/" + "0" + data[0];
			} else if (birthday.length() == 9) {
				String data[] = birthday.split("(/|\\.)");
				if (data[0].length() == 1) {
					return data[2] + "/" + data[1] + "/" + "0" + data[0];
				} else {
					return data[2] + "/" + "0" + data[1] + "/" + data[0];
				}
			} else  if (birthday.length() == 10){
				String data[] = birthday.split("(/|\\.)");
				return data[2] + "/" + data[1] + "/" + data[0];
			} else {
				String data[] = birthday.split("(/|\\.)");
				String string = "";
				for (int i = data.length - 1; i >= 0; i--) {
					string += data[i] +"/";
				}
				return string.substring(0,string.length()-2);
			}
		} else {
			return birthday;
		}
	}
	
	public static String linkToWiki(String singerName) {
		try {
			String newString = singerName.toLowerCase();
			newString = newString.replaceAll("_", " ");
			FileInputStream fis = new FileInputStream(new File("singerWikiList.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			while (br.ready()) {
				String line = br.readLine();
				String data[] = line.split(",");
				if (data[0].toLowerCase().contains(newString)) {
					br.close();
					return data[1];
				}
			}
			
			br.close();
			isr.close();
			fis.close();
			return "";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	public static boolean birthPlace(String htmlFile, String path) {
		if (birthPlaceProvince(htmlFile,path)) {
			return true;
		} else if (birthPlaceDistrict(htmlFile,path)) {
			return true;
		} else if (birthPlaceTown(htmlFile,path)) {
			return true;
		}
		return false;
	}
	
	public static boolean birthPlaceChieuCao(File htmlFile, String path) {
		if (birthPlaceProvinceChieuCao(htmlFile, path)) {
			return true;
		} else if (birthPlaceDistrictChieuCao(htmlFile, path)) {
			return true;
		} else if (birthPlaceTownChieuCao(htmlFile, path)) {
			return true;
		}
		return false;
	}
	
	public static boolean birthPlaceProvinceChieuCao(File htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile, "UTF-8");
			
			Elements elements = doc.getElementsByClass("entry-content");
			
			String data = "";
			
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("p")) {
					data += line.text() + "\n";
				}
			}			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("provinces.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			task1:
			while (br.ready()) {
				
				String line = br.readLine();
				String provinces[] = line.split(",");
				String convert = provinces[0].toLowerCase();
				Pattern pattern = Pattern.compile("(sinh|quê|quán).{0,70}(ở|tại|từ).{0,50}" + convert);
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					result += provinces[1];
					break task1;
				}
			}
			
			if (!result.equals("")) {
				placeLinkToWiki(result, path);
				return true;
			}
						
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean birthPlaceDistrictChieuCao(File htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile, "UTF-8");
			
			Elements elements = doc.getElementsByClass("entry-content");
			
			String data = "";
			
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("p")) {
					data += line.text() + "\n";
				}
			}
			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("districts.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			task1:
			while (br.ready()) {
				
				String line = br.readLine();
				String provinces[] = line.split(",");
				String convert = provinces[0].toLowerCase();
				Pattern pattern = Pattern.compile("(sinh|quê|quán).{0,70}(ở|tại|từ).{0,50}" + convert);
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					result += provinces[1];
					break task1;
				}
			}
			
			if (!result.equals("")) {
				Pattern pattern = Pattern.compile("Tỉnh.*");
				Matcher matcher = pattern.matcher(result);
				if (matcher.find()) {
					result = matcher.group(0);
					placeLinkToWiki(result, path);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						placeLinkToWiki(result, path);
					}
				}
				return true;
			}
						
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean birthPlaceTownChieuCao(File htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile, "UTF-8");
			
			Elements elements = doc.getElementsByClass("entry-content");
			
			String data = "";
			
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data += line.text() + "\n";
				}
			}
			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("towns.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			task1:
			while (br.ready()) {
				
				String line = br.readLine();
				String provinces[] = line.split(",");
				String convert = provinces[0].toLowerCase();
				Pattern pattern = Pattern.compile("(sinh|quê|quán).{0,70}(ở|tại|từ).{0,50}" + convert);
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					result += provinces[1];
					break task1;
				}
			}
			
			if (!result.equals("")) {
				Pattern pattern = Pattern.compile("Tỉnh.*");
				Matcher matcher = pattern.matcher(result);
				if (matcher.find()) {
					result = matcher.group(0);
					placeLinkToWiki(result, path);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						placeLinkToWiki(result, path);
					}
				}
				return true;
			}
						
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean birthPlaceProvince(String htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile);
			
			Elements elements = doc.getElementsByClass("entry");
			
			String data = "";
			
			task:
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data = line.text();
					break task;
				}
			}
			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("provinces.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			task1:
			while (br.ready()) {
				
				String line = br.readLine();
				String provinces[] = line.split(",");
				String convert = provinces[0].toLowerCase();
				Pattern pattern = Pattern.compile("(sinh|quê|quán).{0,70}(ở|tại|từ).{0,50}" + convert);
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					result += provinces[1];
					break task1;
				}
			}
			
			if (!result.equals("")) {
				placeLinkToWiki(result, path);
				return true;
			}
						
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean birthPlaceDistrict(String htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile);
			
			Elements elements = doc.getElementsByClass("entry");
			
			String data = "";
			
			task:
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data = line.text();
					break task;
				}
			}
			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("districts.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			task1:
			while (br.ready()) {
				
				String line = br.readLine();
				String provinces[] = line.split(",");
				String convert = provinces[0].toLowerCase();
				Pattern pattern = Pattern.compile("(sinh|quê|quán).{0,70}(ở|tại|từ).{0,50}" + convert);
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					result += provinces[1];
					break task1;
				}
			}
			
			if (!result.equals("")) {
				Pattern pattern = Pattern.compile("Tỉnh.*");
				Matcher matcher = pattern.matcher(result);
				if (matcher.find()) {
					result = matcher.group(0);
					placeLinkToWiki(result, path);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						placeLinkToWiki(result, path);
					}
				}
				return true;
			}
						
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean birthPlaceTown(String htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile);
			
			Elements elements = doc.getElementsByClass("entry");
			
			String data = "";
			
			task:
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data = line.text();
					break task;
				}
			}
			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("towns.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			task1:
			while (br.ready()) {
				
				String line = br.readLine();
				String provinces[] = line.split(",");
				String convert = provinces[0].toLowerCase();
				Pattern pattern = Pattern.compile("(sinh|quê|quán).{0,70}(ở|tại|từ).{0,50}" + convert);
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					result += provinces[1];
					break task1;
				}
			}
			
			if (!result.equals("")) {
				Pattern pattern = Pattern.compile("Tỉnh.*");
				Matcher matcher = pattern.matcher(result);
				if (matcher.find()) {
					result = matcher.group(0);
					placeLinkToWiki(result, path);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						placeLinkToWiki(result, path);
					}
				}
				return true;
			}
						
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static void placeLinkToWiki(String birthPlace, String path) {
		try {
			Writer writer = new FileWriter(new File(path), true);
			if (birthPlace.contains("Tỉnh")) {
				birthPlace = birthPlace.substring(5);
				birthPlace = birthPlace.replaceAll(" ", "_");
				birthPlace = "https://vi.wikipedia.org/wiki/" + birthPlace;
				writer.append("group1:" + singerName + " group1:birthPlace " + "\"" + birthPlace + "\" .");
				writer.append("\n");
				writer.close();
			} else {
				if (birthPlace.contains("Hồ Chí Minh")) {
					birthPlace = birthPlace.replaceAll(" ", "_");
					birthPlace = "https://vi.wikipedia.org/wiki/" + birthPlace;
					writer.append("group1:" + singerName + " group1:birthPlace " + "\"" + birthPlace + "\" .");
					writer.append("\n");
					writer.close();
				} else {
					birthPlace = birthPlace.substring(10);
					birthPlace = birthPlace.replaceAll(" ", "_");
					birthPlace = "https://vi.wikipedia.org/wiki/" + birthPlace;
					writer.append("group1:" + singerName + " group1:birthPlace " + "\"" + birthPlace + "\" .");
					writer.append("\n");
					writer.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void bands(String htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile);
			
			Elements elements = doc.getElementsByClass("entry");
			
			Writer writer = new FileWriter(new File(path), true);
			
			String data = "";
			
			task:
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data = line.text();
					break task;
				}
			}
			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("bands.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			
			while (br.ready()) {
				
				String line = br.readLine();
				String convert = line.toLowerCase();
				Pattern pattern = Pattern.compile("(nhóm|ban).{0,20}"+ convert);
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					result += line + ", ";
				}
			}
			
			if (result.length() > 0 ) {
				writer.append("group1:" + singerName + " group1:memberOf " + "\"" + result.substring(0, result.length()-2) + "\" .");
				writer.append("\n");
			} else {
				writer.append("group1:" + singerName + " group1:memberOf " + "\"" + result + "\" .");
				writer.append("\n");
			}
						
			
			br.close();
			isr.close();
			fis.close();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void instrument(String htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile);
			
			Elements elements = doc.getElementsByClass("entry");
			
			Writer writer = new FileWriter(new File(path), true);
			
			String data = "";
			
			task:
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data = line.text();
					break task;
				}
			}
			
			
			data = data.toLowerCase();
			
			FileInputStream fis = new FileInputStream(new File("instruments.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			
			while (br.ready()) {
				
				String line = br.readLine();
				String convert = line.toLowerCase();
				if (data.contains(convert)) {
					result += line + ", ";
				}
			}
			
			if (result.length() > 0 ) {
				writer.append("group1:" + singerName + " group1:instrument " + "\"" + result.substring(0, result.length()-2) + "\" .");
				writer.append("\n");
			} else {
				writer.append("group1:" + singerName + " group1:instrument " + "\"" + result + "\" .");
				writer.append("\n");
			}
						
			
			
			
			br.close();
			isr.close();
			fis.close();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void genres(String htmlFile, String path) {
		try {
			Document doc = Jsoup.parse(htmlFile);
			
			Elements elements = doc.getElementsByClass("entry");
			
			Writer writer = new FileWriter(new File(path), true);
			
			String data = "";
			
			task:
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data = line.text();
					break task;
				}
			}
			
			data = data.toLowerCase();
			
			
			FileInputStream fis = new FileInputStream(new File("genres.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String result = "";
			
			
			
			while (br.ready()) {
				
				String line = br.readLine();
				String convert = line.toLowerCase();
				if (data.contains(convert)) {
					result += line + ", ";
				}
			}
			
			if (result.length() > 0 ) {
				writer.append("group1:" + singerName + " group1:profession " + "\"" + result.substring(0, result.length()-2) + "\" .");
				writer.append("\n");
			} else {
				writer.append("group1:" + singerName + " group1:profession " + "\"" + result + "\" .");
				writer.append("\n");
			}
						
			writer.append("\n");
			
			br.close();
			isr.close();
			fis.close();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static String convertHeight(String height) {
		
		String regex1 = "[1-2]{1}\\s*m\\s*[0-9]+";
		String regex2 = "[1-2]{1},[0-9]+\\s*m";
		String regex3 = "[1-2]{1}\\.[0-9]+\\s*m";
		String regex4 = "[0-9]{3}\\s*cm";
		String regex5 = "[1-2]{1},[0-9]+";
		String regex6 = "[1-2]{1}\\.[0-9]+";
		
		if (height.matches(regex1)) {
			String data[] = height.split("m");
			if (data[1].length() == 1) {
				data[1] += "0";
			}
			data[0] = data[0].replaceAll("\\s*", "");
			data[1] = data[1].replaceAll("\\s*", "");
			return Integer.parseInt(data[0])*100 + Integer.parseInt(data[1]) + " cm"; 
		} else if (height.matches(regex2)) {
			String data[] = height.split("\\s*m");
			data[0] = data[0].replaceAll(",", ".");
			Double parse = Double.parseDouble(data[0])*100; 
			return parse.intValue() + " cm";
		} else if (height.matches(regex3)) {
			String data[] = height.split("\\s*m");
			Double parse = Double.parseDouble(data[0])*100;
			return parse.intValue() + " cm";
		} else if (height.matches(regex4)) {
			String data[] = height.split("\\s*cm");
			return data[0] + " cm";
		} else if (height.matches(regex5)) {
			height = height.replaceAll(",", ".");
			Double parse = Double.parseDouble(height)*100;
			return parse.intValue() + " cm";
		} else if (height.matches(regex6)) {
			Double parse = Double.parseDouble(height)*100;
			return parse.intValue() + " cm";
		}
		
		return height;
	}
	
	public static boolean analysisHeight(String data, String path) {
		try {
			String regex = "[1-2]{1}\\s*m\\s*[0-9]+|[1-2]{1},[0-9]+\\s*m|[1-2]{1},[0-9]+|[1-2]{1}\\.[0-9]+\\s*m|[1-2]{1}\\.[0-9]+|[0-9]{3}\\s*cm";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(data);
			
			Writer writer = new FileWriter(new File(path), true);
			
			if (matcher.find()) {
				writer.append("group1:" + singerName + " group1:Height " + "\"" + convertHeight(matcher.group(0)) + "\" .");
				writer.append("\n");
				writer.close();
				return true;
			}
			writer.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Page> loadData() {
		ArrayList<Page> list = new ArrayList<>();
		String singerFolder = "data/chieu-cao.net/category/ca-sy/page";
		try {
			File folder = new File(singerFolder);
			if (folder.isDirectory()) {
				for (File file : folder.listFiles()) {
					Document doc = Jsoup.parse(file, "UTF-8");

					Elements links = doc.getElementsByTag("a");
					for (Element line : links) {
						Page singerPage = new Page();
						singerPage.setTitle(line.text());
						singerPage.setUrl(line.attr("href"));
						list.add(singerPage);
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	public static boolean chieucao(File htmlFile, String path) {
		boolean checkHeight = false;
		try {
			Document doc = Jsoup.parse(htmlFile, "UTF-8");
			Elements dataClass = doc.getElementsByClass("entry-content");
			
			task:
			for (Element elementOfData: dataClass) {
				for (Element line: elementOfData.getElementsByTag("p")) {
					String data = line.text();
					if (analysisHeight(data, path)) {
						checkHeight = true;
						break task;
					}
				}
				
				for (Element line: elementOfData.getElementsByTag("div")) {
					String data = line.text();
					if (analysisHeight(data, path)) {
						checkHeight = true;
						break task;
					}
				}
			}
			return checkHeight;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static void analysis(String singerName, ArrayList<Page> list, String path) {
		
		ArrayList<String> singerPath = new ArrayList<>();

		for (Page element : list) {
			if (element.getTitle().toLowerCase().contains(singerName.toLowerCase())) {
				singerPath.add(element.convertUrlToPath());
			}
		}

		try {
			Writer writer = new FileWriter(new File(path), true);
			
			for (String element : singerPath) {
				try {
					File htmlFile = new File(element);
					if (chieucao(htmlFile, path)) {
						return;
					}
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			writer.append("group1:" + singerName + " group1:Height " + "\"" + "" + "\" .");
			writer.append("\n");
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void linkToWikiFunc(String singerName, String path) {
		try {
			Writer writer = new FileWriter(new File(path), true);
			
			writer.append("group1:" + singerName + " group1:linkToWiki " + "\"" + linkToWiki(singerName) + "\" .");
			writer.append("\n");
			
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public static void analysisPlace(String singerName, ArrayList<Page> list, String path) {
		ArrayList<String> singerPath = new ArrayList<>();

		for (Page element : list) {
			if (element.getTitle().toLowerCase().contains(singerName.toLowerCase())) {
				singerPath.add(element.convertUrlToPath());
			}
		}
		try {
			Writer writer = new FileWriter(new File(path), true);
			
			for (String element : singerPath) {
				try {
					File htmlFile = new File(element);
					if (birthPlaceChieuCao(htmlFile, path)) {
						return;
					}
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			writer.append("group1:" + singerName + " group1:birthPlace " + "\"" + "" + "\" .");
			writer.append("\n");
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void read(String path) {
		boolean check = false;
		try {
			File newFile = new File(path);
			if (newFile.isDirectory()) {
				File listFile[] = newFile.listFiles();
				for (int i = 0; i < listFile.length; i++) {
					String data = "";
					String filePath = "database/singerDB/singerDB" + countFile + ".ttl";
					
					FileInputStream fis = new FileInputStream(listFile[i]);
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);

					while (br.ready()) {

						String line = br.readLine();
						if (line.equals("<html lang=\"vi\">")) {
							check = true;
						}
						if (check) {
							data += line + "\n";
						}
						if (line.contains("</html>")) {
							check = false;
							if (countEntity % 200 == 0) {
								countFile++;
								filePath = "database/singerDB/singerDB" + countFile + ".ttl";
							}
							String singerName = information(data,filePath);
							if (!singerName.equals("")) {
								ArrayList<Page> list = loadData();
								linkToWikiFunc(singerName, filePath);
								analysis(singerName, list, filePath);
								if (birthPlace(data, filePath)) {
									
								} else {
									analysisPlace(singerName, list, filePath);
								}
								bands(data, filePath);
								instrument(data, filePath);
								genres(data, filePath);
								countEntity++;									
							}
							data = "";
						}
							
					}
					
					br.close();
					isr.close();
					fis.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
