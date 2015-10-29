package pipe;

import sq.data.Instance;
import sq.pipe.Pipe;

public class TokenLineLexical extends Pipe {

	private static final long serialVersionUID = 1302379148805014167L;

	public Instance proc(Instance inst) {
		for (int i = 0; i < inst.length(); i++) {
			String text = inst.getText(i).trim();
			for (int j = 0; j < text.length(); j++) {
				if (Character.isLetterOrDigit(text.charAt(j)))
					break;
				char s = text.charAt(j);
				if (s == ';' || s == '>' || s == ';' || s == '#' || s == '%') {
					inst.addUnary(i, "SYM");
					inst.addBinary(i, "SYM");
				}
			}
			if (text.contains(":") || text.contains("..") || text.contains("]") || text.contains("\t")) {
				inst.addUnary(i, "SEP");
				inst.addBinary(i, "SEP");
			}
			String[] parts = text.split(":|\\.\\.|]|\t");
			int c;
			field: for (c = 0; c < parts.length - 1; c++) {
				if (parts[c].length() > 0) {
					for (int j = 0; j < parts[c].length(); j++) {
						if (Character.isLetterOrDigit(parts[c].charAt(j)))
							break field;
					}
				}
			}
			String[] words;
			if (c < parts.length - 1 || text.endsWith(":") || text.endsWith("]")) {
				words = parts[c].split("[^a-zA-Z0-9']");
				for (int j = 0; j < words.length; j++) {
					if (words[j].length() > 0) {
						inst.addUnary(i, words[j].toLowerCase() + "@T");
						inst.addBinary(i, words[j].toLowerCase() + "@T");
					}
				}
				++c;
			} else {
				words = parts[c].split("[^a-zA-Z0-9']");
				if (words.length < 4) {
					for (int j = 0; j < words.length; j++) {
						if (words[j].length() > 0) {
							inst.addUnary(i, words[j].toLowerCase() + "@T");
							inst.addBinary(i, words[j].toLowerCase() + "@T");
						}
					}
					++c;
				}
			}
			for (; c < parts.length; c++) {
				if (parts[c].length() > 0) {
					words = parts[c].split("[^a-zA-Z0-9']");
					for (int j = 0; j < words.length; j++) {
						if (words[j].length() > 0) {
							inst.addUnary(i, words[j].toLowerCase() + "@V");
						}
					}
				}
			}
		}
		return inst;
	}
}