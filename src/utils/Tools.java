package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	public static PrintStream out;

	static {
		try {
			out = new PrintStream(System.out, true, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String readFile(String path, String encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void writeFile(String path, String text, String encoding) throws IOException {
		Files.write(Paths.get(path), text.getBytes(encoding));
	}

	public static String readFile(String path) throws IOException {
		return readFile(path, "UTF-8");
	}

	public static void writeFile(String path, String text) throws IOException {
		Files.write(Paths.get(path), text.getBytes("UTF-8"));
	}

	public static HashSet<String> readFileToSet(String path) throws IOException {
		HashSet<String> hashSet = new HashSet<>();
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.length() > 0) {
				hashSet.add(line);
			}
		}
		br.close();
		return hashSet;
	}
	
	public static List<String> readFileToList(String path) throws IOException {
		List<String> list = new LinkedList<>();
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		br.close();
		return list;
	}
	
	public static ArrayList<String> readFileToArray(String path) throws IOException {
		ArrayList<String> list = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.length() > 0) {
				list.add(line);
			}
		}
		br.close();
		return list;
	}
	
	public static void writeSetToFile(String path, Set<String> set) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		for (String line : set) {
			bw.append(line);
			bw.newLine();
		}
		bw.close();
	}
	
	public static void writeListToFile(String path, List<String> list) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		for (String line : list) {
			bw.append(line);
			bw.newLine();
		}
		bw.close();
	}

	public static ArrayList<String> getLinks(String text) {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while (m.find()) {
			String urlStr = m.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}
			// System.out.println(urlStr.indexOf('.'));
			if (urlStr.indexOf('.') < 0)
				continue;
			links.add(urlStr);
		}
		return links;
	}

	public static String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain;
		// return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	public static String getDomain(String url) {
		String domain;
		if (url.startsWith("http://")) {
			domain = url.substring(7);
		} else if (url.startsWith("https://")) {
			domain = url.substring(8);
		} else {
			domain = url;
		}
		domain = domain.split("/|:")[0];
		return domain;
	}
	
	public static List<Set<String>> nFold(Set<String> data, int fold) {
		List<Set<String>> setList = new ArrayList<>();
		for (int i = 0; i < fold; i++) {
			setList.add(new HashSet<>());
		}
		for (String point : data) {
			setList.get((int) (Math.random()*fold)).add(point);
		}
		return setList;
	}
}