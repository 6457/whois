package pipe;

import sq.data.Instance;
import sq.pipe.Pipe;

public class TokenLineStructure extends Pipe {
	private static final long serialVersionUID = -3059757367105532822L;

	public Instance proc(Instance inst) {
		int idt = 0;
		for (int i = 0; i < inst.length(); i++) {
			String text = inst.getText(i);
			char s = text.charAt(0);
			int j = 0;
			if (s == '\n') {
				inst.addUnary(i, "NEL");
				inst.addBinary(i, "NEL");
				j = 1;
			}
			int k = 0;
			indent: while (j < text.length()) {
				switch (text.charAt(j)) {
				case ' ':
					++k;
					++j;
					break;
				case '\t':
					k += 2;
					++j;
					break;
				default:
					break indent;
				}
			}
			if (k > idt) {
				inst.addUnary(i, "SHR");
				inst.addBinary(i, "SHR");
			} else if (k < idt) {
				inst.addUnary(i, "SHL");
				inst.addBinary(i, "SHL");
			}
			idt = k;
		}
		return inst;
	}

}