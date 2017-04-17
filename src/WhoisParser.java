import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import rant.parser.RantParser;
import sq.model.CRF;
import thick.parser.ThickParser;

public class WhoisParser {
	ThickParser thick;
	RantParser rant;
	
	public WhoisParser() {
		thick = new ThickParser(new CRF(new File("mod/crf170417")));
		rant = new RantParser(new CRF(new File("mod/ran170416")));
	}
	
	public String parseOne(String whois){
		String rantWHOIS = thick.getRant(whois);
		HashMap<String, String> fields = rant.getFields(rantWHOIS);
		return fields.toString();
	}
	
	public static void main(String[] args) {
		WhoisParser wp = new WhoisParser();
		String input = args[0];
		try {
			Files.createDirectories(Paths.get("out"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File folder = new File(input);
		File[] whoises = folder.listFiles();
		if (whoises != null) {
			for (File whois : whoises) {
				System.out.println(whois.getName());
				try {
					String content = new String(Files.readAllBytes(Paths.get(whois.getPath())));
					String parsed = wp.parseOne(content);
					Files.write(Paths.get("out/" + whois.getName()), parsed.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
