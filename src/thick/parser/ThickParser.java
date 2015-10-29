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
	
	public LabelSequence parse(String thickWHOIS) {
		Instance inst = new Instance(ThickFactory.chunk(thickWHOIS));
		return parse(inst);
	}
}