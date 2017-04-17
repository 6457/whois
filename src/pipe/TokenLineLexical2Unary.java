package pipe;

import java.io.Serializable;
import java.util.regex.Pattern;

import sq.data.Instance;
import sq.pipe.Pipe;

public class TokenLineLexical2Unary extends Pipe implements Serializable {
	private static final long serialVersionUID = 1L;

	public Instance proc(Instance inst) {
		int l = inst.length();
		for (int i = 0; i < l; i++) {
			FieldEntity fe = new FieldEntity(inst.getText(i));
			String field = fe.getField();
			if (field == null) {
				inst.addUnary(i, "EPF");
				inst.addBinary(i, "EPF");
			} else {
				String[] words = field.split("[^a-zA-Z0-9']");
				for (int j = 0; j < words.length; j++) {
					inst.addUnary(i, words[j].toLowerCase() + "$F");
					inst.addBinary(i, words[j].toLowerCase() + "$F");
				}
			}
			String entity = fe.getEntity();
			if (entity == null) {
				inst.addUnary(i, "EPE");
				inst.addBinary(i, "EPE");
				continue;
			}
			if (entity.endsWith(".")) {
				inst.addUnary(i, "TDT");
				inst.addBinary(i, "TDT");
			} else if (entity.contains(".")) {
				inst.addUnary(i, "IDT");
				inst.addBinary(i, "IDT");
			}
			if (entity.startsWith("+")) {
				inst.addUnary(i, "PLS");
				inst.addBinary(i, "PLS");
			}
			String[] tokens = entity.split("\\s|,");
			for (String value : tokens) {
				if (value.length() == 0) {
					continue;
				}
				if (isInitCap(value)) {
					inst.addUnary(i, "CAP");
					inst.addBinary(i, "CAP");
				}
				if (isNumber(value)) {
					String rv = value.replaceAll("[^\\d]", "");
					int vl = rv.length();
					if (vl < 3) {
						inst.addUnary(i, "NS3");
						inst.addBinary(i, "NS3");
					} else if (vl < 5) {
						inst.addUnary(i, "NS5");
						inst.addBinary(i, "NS5");
					} else if (vl == 5) {
						inst.addUnary(i, "NE5");
						inst.addBinary(i, "NE5");
					} else if (vl < 10) {
						inst.addUnary(i, "NSX");
						inst.addBinary(i, "NSX");
					} else {
						inst.addUnary(i, "NGX");
						inst.addBinary(i, "NGX");
					}
				}
				if (isEmail(value)) {
					inst.addUnary(i, "EML");
					inst.addBinary(i, "EML");
				}
				if (isAllCap(value)) {
					inst.addUnary(i, "ACP");
					inst.addBinary(i, "ACP");
				}
			}
			tokens = entity.split("[^a-zA-Z0-9']");
			for (String value : tokens) {
				inst.addUnary(i, value.toLowerCase() + "@V");
				inst.addBinary(i, value.toLowerCase() + "@V");
			}
		}
		return inst;
	}

	public static boolean isInitCap(String str) {
		int value = str.charAt(0);
		return (value >= 'A' && value <= 'Z');
	}

//	public static boolean isAllCap(String str) {
//		char[] values = str.toCharArray();
//		for (int i = 0; i < values.length; i++) {
//			if (Character.isLetter(values[i])) {
//				if (!(values[i] >= 'A' && values[i] <= 'Z')) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
	
	public static boolean isAllCap(String str) {
		return str.matches("^[A-Z]+$");
	}

//	public static boolean isNumber(String str) {
//		char[] values = str.toCharArray();
//		for (int i = 0; i < values.length; i++) {
//			if (Character.isLetter(values[i])) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	public static boolean isNumber(String str) {
		return str.matches("^([0-9]|\\.|-|\\+)+");
	}

	public static boolean isEmail(String str) {
		return Pattern.matches(
				"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", str);
	}

	public static void main(String[] args) {
		String str = "";
		System.out.println(isAllCap(str));
	}
}