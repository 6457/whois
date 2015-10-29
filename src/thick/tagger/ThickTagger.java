package thick.tagger;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import sq.data.Alphabet;
import sq.data.LabelSequence;
import sq.data.TokenSequence;
import thick.ThickTitleValue;
import utils.Tools;

public class ThickTagger {
	HashMap<String, Integer> labelset;
	HashSet<String> leadset;
	HashSet<String> nilset;
	HashSet<String> nitset;
	HashSet<String> antset;
	HashSet<String> typeset;
	Pattern datpat;
	Alphabet dict;

	public ThickTagger() {
		labelset = new HashMap<>();
		leadset = new HashSet<>();
		nilset = new HashSet<>();
		nitset = new HashSet<>();
		antset = new HashSet<>();
		typeset = new HashSet<>();
		datpat = Pattern.compile(ThickTags.dat);
		dict = new Alphabet();
		dict.add("NULL");
	}

	public LabelSequence tag(TokenSequence ts) {
		int n = ts.length();
		int[] labind = new int[n];
		final int nind = dict.look("NULL");
		final int aind = dict.look("registrant");
		final int dind = dict.look("date");
		final int mind = dict.look("domain");
		final int oind = dict.look("other");
		int cxt = nind;
		for (int i = 0; i < n; i++) {
			String txt = ts.get(i).getText();
			String ttxt = txt.trim();
			if (datpat.matcher(ttxt).matches()) {
				labind[i] = dind;
				cxt = nind;
				continue;
			}
			if (nilset.contains(ttxt)) {
				labind[i] = nind;
				cxt = nind;
				continue;
			}
			if (ttxt.matches("^NS[1-4]?\\.[A-Z]*\\.COM(\\s+NS[1-4]?\\.[A-Z]*\\.COM)+$")
					|| ttxt.matches(
							"^NS[1-4]?\\.[A-Z]*\\.COM    ([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$.*")
					|| ttxt.equals("WARRIOR.NII.NET   ARCHER.NII.NET")) {
				labind[i] = mind;
				cxt = nind;
				continue;
			}
			ThickTitleValue tv = new ThickTitleValue(txt);
			String field = tv.getField();
			if (tv.getStatus() == 1) {
				labind[i] = nind;
				cxt = nind;
				continue;
			}
			String title;
			if (field == null || typeset.contains(field)) {
				title = tv.getValue();
			} else {
				title = field;
			}
			if (nitset.contains(title)) {
				labind[i] = nind;
				cxt = nind;
				continue;
			}
			Integer labvalue = labelset.get(title);
			if (labvalue == null) {
				if (cxt == nind) {
					labind[i] = nind;
				} else {
					labind[i] = cxt;
				}
			} else {
				if ((cxt == aind || cxt == oind) && antset.contains(title)) {
					labind[i] = cxt;
				} else {
					labind[i] = labvalue;
					if (leadset.contains(title)) {
						cxt = labvalue;
					} else {
						cxt = nind;
					}
				}
			}

		}
		return new LabelSequence(dict, labind);
	}

	public ThickTagger addFromOMN() {
		String[][] keysets = ThickTags.keys;
		for (int i = 0; i < keysets.length; i++) {
			String dname = ThickTags.names[i];
			int value = dict.add(dname);
			for (String key : keysets[i]) {
				labelset.put(key, value);
			}
		}
		String[] leads = ThickTags.lead;
		for (int i = 0; i < leads.length; i++) {
			leadset.add(leads[i]);
		}
		String[] nils = ThickTags.nil;
		for (int i = 0; i < nils.length; i++) {
			nilset.add(nils[i]);
		}
		String[] nits = ThickTags.nit;
		for (int i = 0; i < nits.length; i++) {
			nitset.add(nits[i]);
		}
		String[] ants = ThickTags.ant;
		for (int i = 0; i < ants.length; i++) {
			antset.add(ants[i]);
		}
		String[] types = ThickTags.type;
		for (int i = 0; i < types.length; i++) {
			typeset.add(types[i]);
		}
		return this;
	}

	public ThickTagger addFromFile(String path) throws IOException {
		List<String> lines = Tools.readFileToList(path);
		int curlab = -1;
		HashSet<String> curset = null;
		for (String line : lines) {
			if (line.startsWith("@")) {
				curlab = dict.add(line.substring(1));
				curset = null;
			} else if (line.startsWith("$")) {
				String setname = line.substring(1);
				switch (setname) {
				case "LEAD":
					curset = leadset;
					break;
				case "TYPE":
					curset = typeset;
					break;
				case "ANT":
					curset = antset;
					break;
				case "NIL":
					curset = nilset;
					break;
				case "NIT":
					curset = nitset;
					break;
				default:
					break;
				}
				curlab = -1;
			} else {
				if (curset == null && curlab >= 0) {
					labelset.put(line, curlab);
				} else if (curlab < 0 && curset != null) {
					curset.add(line);
				} else {
					System.err.println("Invalid input condition, ignored!");
				}
			}
		}
		return this;
	}

	public Alphabet getAlphabet() {
		return dict;
	}

	public static boolean isChar(char x) {
		return (x >= 65) && (x < 91) || (x >= 97) && (x < 123);
	}

	public static boolean isDigit(char x) {
		return (x >= 48) && (x < 58);
	}
}