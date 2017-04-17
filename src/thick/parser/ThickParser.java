package thick.parser;

import sq.data.Instance;
import sq.data.LabelSequence;
import sq.model.Model;
import thick.ThickFactory;

public class ThickParser {
	Model model;
	public ThickParser(Model parseModel){
		model = parseModel;
	}
	
	public LabelSequence parse(Instance inst) {
		model.getList().proc(inst);
		return model.getLabel(inst);
	}
	
	public String getRant(String thickWHOIS) {
		Instance inst = new Instance(ThickFactory.chunk(thickWHOIS));
		int[] labels = parse(inst).getLabels();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < labels.length; i++) {
			if (labels[i] == 3) {
				sb.append(inst.getText(i) + "\n");
			}
		}
		return sb.toString();
	}
}