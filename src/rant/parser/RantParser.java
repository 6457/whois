package rant.parser;

import java.util.HashMap;

import rant.RantTitleValue;
import sq.data.Alphabet;
import sq.data.Instance;
import sq.data.LabelSequence;
import sq.model.Model;
import thick.ThickFactory;

public class RantParser {
	Model model;
	
	String[] titles = {
			"NULL",
			"id",
			"name",
			"org",
			"street",
			"city",
			"state",
			"post",
			"country",
			"phone",
			"fax",
			"email",
	};
	
	int[] index;
	
	public RantParser(Model parseModel){
		model = parseModel;
		Alphabet dict = model.getList().getLabelAlphabet();
		index = new int[titles.length];
		for (int i = 0; i < titles.length; i++) {
			index[i] = dict.add(titles[i]);
		}
	}
	
	public LabelSequence parse(Instance inst) {
		model.getList().proc(inst);
		return model.getLabel(inst);
	}
	
	public HashMap<String, String> extract(Instance inst) {
		StringBuilder[] sbs = new StringBuilder[titles.length];
		for (int i = 0; i < sbs.length; i++) {
			sbs[i] = new StringBuilder();
		}
		int[] ls = parse(inst).getLabels();
		for (int i = 0; i < ls.length; i++) {
			String st = new RantTitleValue(inst.getText(i)).getValue();
			if (st == null) {
				continue;
			}
			sbs[ls[i]].append(st.trim() + "\n");
		}
		HashMap<String, String> rant = new HashMap<>();
		for (int i = 0; i < titles.length; i++) {
			rant.put(titles[i], sbs[i].toString().trim());
		}
		return rant;
	}
	
	public HashMap<String, String> getFields(String thickWHOIS) {
		Instance inst = new Instance(ThickFactory.chunk(thickWHOIS));
		return extract(inst);
	}
}
