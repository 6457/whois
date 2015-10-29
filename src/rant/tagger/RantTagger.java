package rant.tagger;

import java.util.HashSet;

import rant.RantTitleValue;
import sq.data.Alphabet;
import sq.data.LabelSequence;
import sq.data.TokenSequence;

public class RantTagger {
	Alphabet dict;
	HashSet<String> ld;
	final int ni; // null
	final int ii; // id
	final int ai; // name
	final int oi; // org
	final int si; // street
	final int ci; // city
	final int ti; // state
	final int pi; // postcode
	final int yi; // country
	final int hi; // phone
	final int fi; // fax
	final int ei; // email
	
	public static String[] lead =  {
			"OWNER",
			"owner",
			"Owner-c",
			"holder",
			"REGISTRANT",
			"Registrant",
			"registrant",
			"Registant",
			"Registrant Contact",
			"Registrant contact",
			"Registrant Contact Details",
			"REGISTRANT CONTACT INFO",
			"Registrant Contact Information",
			"Registrant Details",
			"Registrant Info",
			};

	public RantTagger() {
		dict = new Alphabet();
		ni = dict.add("NULL");// 0
		ii = dict.add("id");// 1
		ai = dict.add("name");// 2
		oi = dict.add("org");// 3
		si = dict.add("street");// 4
		ci = dict.add("city");// 5
		ti = dict.add("state");// 6
		pi = dict.add("post");// 7
		yi = dict.add("country");// 8
		hi = dict.add("phone");// 9
		fi = dict.add("fax");// 10
		ei = dict.add("email");// 11
		ld = new HashSet<>();
		for (int i = 0; i < lead.length; i++) {
			ld.add(lead[i]);
		}
	}

	public LabelSequence tag(TokenSequence docum) {
		int l = docum.length();
		int[] labs = new int[l];
		int cxt = ni;

		for (int i = 0; i < l; i++) {
			String text = docum.get(i).getText();
			if (text.startsWith("Adress")) {
				labs[i] = si;
				cxt = ni;
				continue;
			}

			if (text.startsWith("locality:")) {
				labs[i] = ti;
				cxt = ni;
				continue;
			}
			RantTitleValue tv = new RantTitleValue(text);
			String field = tv.getTitle();
			if (field == null) {
				String entity = tv.getValue();
				if (ld.contains(entity)) {
					labs[i] = ni;
					cxt = ni;
					continue;
				} else if (cxt != ni) {
					labs[i] = cxt;
					if (i > 1 && docum.get(i - 1).getText().startsWith("Address:")
							&& docum.get(i - 2).getText().startsWith("Name:")) {
						return null;
					}
					continue;
				}
				if (i > 0 && docum.get(i - 1).getText().equals("[Registrant]")) {
					labs[i] = oi;
					cxt = ni;
					continue;
				}
				return null;
			} else if (ld.contains(field)) {
				if (tv.getTitle() == null) {
					labs[i] = ni;
				} else {
					labs[i] = ai;
				}
				cxt = ni;
				continue;
			}
			String txt = field.toLowerCase();
			if (txt.contains("city")) {
				labs[i] = ci;
				cxt = ni;
			} else if (txt.contains("country")) {
				labs[i] = yi;
				cxt = ni;
			} else if (txt.contains("phone")) {
				labs[i] = hi;
				cxt = ni;
			} else if (txt.contains("fax")) {
				labs[i] = fi;
				cxt = ni;
			} else if (txt.contains("id") || txt.contains("handle")) {
				labs[i] = ii;
				cxt = ni;
			} else if (txt.contains("code") || txt.contains("organization pc") || txt.contains("zip")) {
				labs[i] = pi;
				cxt = ni;
			} else if (txt.contains("state") || txt.contains("province")) {
				labs[i] = ti;
				cxt = ni;
			} else if (txt.contains("email") || txt.contains("e-mail")) {
				labs[i] = ei;
				cxt = ni;
			} else if (txt.contains("street") || txt.contains("address")) {
				labs[i] = si;
				cxt = si;
			} else if (txt.contains("name") || txt.equals("contact")) {
				if (txt.equals("organization name")
						&& i < l & docum.get(i + 1).getText().startsWith("Organization Org")) {
					labs[i] = oi;
				} else {
					labs[i] = ai;
				}
				if (txt.equals("registrant name")) {
					cxt = ai;
				} else {
					cxt = ni;
				}
			} else if (txt.contains("org") || txt.equals("company")) {
				labs[i] = oi;
				if (txt.equals("registrant organization")) {
					cxt = oi;
				} else {
					cxt = ni;
				}
			} else {
				labs[i] = ni;
				cxt = ni;
			}
		}
		return new LabelSequence(dict, labs);
	}

	public Alphabet getAlphabet() {
		return dict;
	}

	public static void main(String[] args) {
		String text = "country   : BR";
		String[] values = text.split("\n");
		RantTagger tagger = new RantTagger();
		Alphabet alpha = tagger.getAlphabet();
		LabelSequence ls = tagger.tag(new TokenSequence(values));
		for (int i = 0; i < ls.length(); i++) {
			System.out.println(values[i] + "::" + alpha.get(ls.get(i)));
		}
	}
}