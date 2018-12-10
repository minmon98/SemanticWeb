package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class testWiki {
	public static void main(String[] args) {
//		instrument("https://mp3.zing.vn/nghe-si/Ung-Dai-Ve/tieu-su");
//		genres("https://mp3.zing.vn/nghe-si/Toc-Tien/tieu-su");
//		bands("https://mp3.zing.vn/nghe-si/Ung-Dai-Ve/tieu-su");
//		birthPlace("https://mp3.zing.vn/nghe-si/Toc-Tien/tieu-su");
//		ArrayList<Page> list = loadData();
//		analysis("Bằng Kiều", list);
//		analysisPlace("Đàm Vĩnh Hưng", list);
		
//		System.out.println(linkToWiki("Tóc Tiên"));
		
		convertBirthDay("1998");
	}
	
	public static void convertBirthDay(String birthday) {
		if (birthday.length() == 10) {
			System.out.println(birthday);
		} else if (birthday.length() == 7) {
			System.out.println("dd/" + birthday);
		} else if (birthday.length() == 5) {
			System.out.println(birthday + "/yyyy");
		} else if (birthday.length() == 4) {
			System.out.println("dd/mm/" + birthday);
		} else {
			System.out.println("dd/mm/yyyy");
		}
	}
	
	public static String linkToWiki(String singerName) {
		try {
			FileInputStream fis = new FileInputStream(new File("singerWikiList.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			while (br.ready()) {
				String line = br.readLine();
				String data[] = line.split(",");
				if (data[0].contains(singerName)) {
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
	
	public static boolean birthPlace(String url) {
		if (birthPlaceProvince(url)) {
			return true;
		} else if (birthPlaceDistrict(url)) {
			return true;
		} else if (birthPlaceTown(url)) {
			return true;
		}
		return false;
	}
	
	public static boolean birthPlaceChieuCao(File htmlFile) {
		if (birthPlaceProvinceChieuCao(htmlFile)) {
			return true;
		} else if (birthPlaceDistrictChieuCao(htmlFile)) {
			return true;
		} else if (birthPlaceTownChieuCao(htmlFile)) {
			return true;
		}
		return false;
	}
	
	public static boolean birthPlaceProvinceChieuCao(File htmlFile) {
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
				System.out.println(result);
				placeLinkToWiki(result);
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
	
	public static boolean birthPlaceDistrictChieuCao(File htmlFile) {
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
					System.out.println(result);
					placeLinkToWiki(result);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						System.out.println(result);
						placeLinkToWiki(result);
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
	
	public static boolean birthPlaceTownChieuCao(File htmlFile) {
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
					System.out.println(result);
					placeLinkToWiki(result);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						System.out.println(result);
						placeLinkToWiki(result);
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
	
	public static boolean birthPlaceProvince(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			
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
				System.out.println(result);
				placeLinkToWiki(result);
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
	
	public static boolean birthPlaceDistrict(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			
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
					System.out.println(result);
					placeLinkToWiki(result);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						System.out.println(result);
						placeLinkToWiki(result);
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
	
	public static boolean birthPlaceTown(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			
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
					System.out.println(result);
					placeLinkToWiki(result);
				} else {
					pattern = Pattern.compile("Thành phố.*");
					matcher = pattern.matcher(result);
					if (matcher.find()) {
						result = matcher.group(0);
						System.out.println(result);
						placeLinkToWiki(result);
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
	
	public static void placeLinkToWiki(String birthPlace) {
		if (birthPlace.contains("Tỉnh")) {
			birthPlace = birthPlace.substring(5);
			birthPlace = birthPlace.replaceAll(" ", "_");
			birthPlace = "https://vi.wikipedia.org/wiki/" + birthPlace;
			System.out.println(birthPlace);
		} else {
			if (birthPlace.contains("Hồ Chí Minh")) {
				birthPlace = birthPlace.replaceAll(" ", "_");
				birthPlace = "https://vi.wikipedia.org/wiki/" + birthPlace;
				System.out.println(birthPlace);
			} else {
				birthPlace = birthPlace.substring(10);
				birthPlace = birthPlace.replaceAll(" ", "_");
				birthPlace = "https://vi.wikipedia.org/wiki/" + birthPlace;
				System.out.println(birthPlace);
			}
		}
	}
	
	public static void bands(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			
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
				System.out.println(result.substring(0, result.length()-2));
			} else {
				System.out.println(result);
			}
						
			
			br.close();
			isr.close();
			fis.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void instrument(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			
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
				System.out.println(result.substring(0, result.length()-2));
			} else {
				System.out.println(result);
			}
						
			
			br.close();
			isr.close();
			fis.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void genres(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			
			Elements elements = doc.getElementsByClass("entry");
			
			String data = "";
			
			task:
			for (Element element: elements) {
				for (Element line: element.getElementsByTag("div")) {
					data = line.text();
					break task;
				}
			}
			
			
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
				System.out.println(result.substring(0, result.length()-2));
			} else {
				System.out.println(result);
			}
						
			
			br.close();
			isr.close();
			fis.close();
			
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
	
	public static boolean analysisHeight(String data) {
		String regex = "[1-2]{1}\\s*m\\s*[0-9]+|[1-2]{1},[0-9]+\\s*m|[1-2]{1},[0-9]+|[1-2]{1}\\.[0-9]+\\s*m|[1-2]{1}\\.[0-9]+|[0-9]{3}\\s*cm";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		 
		if (matcher.find()) {
			System.out.println(convertHeight(matcher.group(0)));
			return true;
		}
		return false;
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
	
	public static boolean chieucao(File htmlFile) {
		boolean checkHeight = false;
		try {
			Document doc = Jsoup.parse(htmlFile, "UTF-8");
			Elements dataClass = doc.getElementsByClass("entry-content");
			
			task:
			for (Element elementOfData: dataClass) {
				for (Element line: elementOfData.getElementsByTag("p")) {
					String data = line.text();
					if (analysisHeight(data)) {
						checkHeight = true;
						break task;
					}
				}
				
				for (Element line: elementOfData.getElementsByTag("div")) {
					String data = line.text();
					if (analysisHeight(data)) {
						checkHeight = true;
						break task;
					}
				}
			}
			if (!checkHeight) {
				System.out.println("");
			}
			
			return checkHeight;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static void analysis(String singerName, ArrayList<Page> list) {
		
		ArrayList<String> singerPath = new ArrayList<>();

		for (Page element : list) {
			if (element.getTitle().contains(singerName)) {
				singerPath.add(element.convertUrlToPath());
			}
		}

		for (String element : singerPath) {
			try {
				File htmlFile = new File(element);
				if (chieucao(htmlFile)) {
					return;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void analysisPlace(String singerName, ArrayList<Page> list) {
		ArrayList<String> singerPath = new ArrayList<>();

		for (Page element : list) {
			if (element.getTitle().contains(singerName)) {
				singerPath.add(element.convertUrlToPath());
			}
		}

		for (String element : singerPath) {
			try {
				File htmlFile = new File(element);
				if (birthPlaceChieuCao(htmlFile)) {
					return;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
