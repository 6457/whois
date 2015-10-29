package thick;

import java.util.LinkedList;

import sq.data.TokenSequence;

public class ThickFactory {
	public static TokenSequence chunk(String thickWHOIS) {
		String[] lines = thickWHOIS.split("\n");
		LinkedList<String> tost = new LinkedList<String>();
		boolean newline = false;
		for (String line : lines) {
			int l = line.length();
			int j = 0;
			while (j < l && !Character.isLetterOrDigit(line.charAt(j)))
				++j;
			if (j == l) {
				newline = true;
			} else {
				if (newline) {
					line = "\n" + line;
				}
				tost.add(line);
				newline = false;
			}
		}
		if (tost.size() == 0) {
			System.err.println("Empty data!");
		}
		return new TokenSequence(tost);
	}
}