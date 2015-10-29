package rant;

public class RantTitleValue {
	char[] value;
	String field;
	String entity;
	int i;
	int j;
	int s;
	int l;

	public RantTitleValue(String src) {
		i = 0;
		j = 0;
		value = src.toCharArray();
		l = value.length;
		nextChar();
		s = i;
		while ((i < l) && (!isPunc()))
			++i;
		j = i;
		while (!(Character.isLetterOrDigit(value[j - 1]) || value[j - 1] == '.'))
			--j;
		if (i == l) {
			field = null;
			entity = new String(value, s, j - s);
		} else {
			field = new String(value, s, j - s);
			nextChar();
			if (i == l) {
				entity = null;
			} else {
				s = i;
				entity = new String(value, s, l - s);
			}
		}
	}

	public String getTitle() {
		return field;
	}

	public String getValue() {
		return entity;
	}

	public void nextChar() {
		while ((i < l) && !(Character.isLetterOrDigit(value[i]) || value[i] == '+')) {
			++i;
		}
	}

	public boolean isPunc() {
		if (value[i] == '.' && i < l - 1 && value[i + 1] == '.') {
			return true;
		}
		if (value[i] == ' ' && i < l - 2 && value[i + 1] == ' ' && value[i + 2] == ' ') {
			return true;
		}
		return value[i] == ':' || value[i] == '\t' || value[i] == ']' || value[i] == '[';
	}
}