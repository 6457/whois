package thick;

public class ThickTitleValue {
	char[] value;
	String field;
	String entity;
	int status;// 0: normal; 1: system; 2: indentation.
	int i;
	int j;
	int s;
	int l;

	public ThickTitleValue(String src) {
		i = 0;
		j = 0;
		s = 0;
		status = 0;
		value = src.toCharArray();
		l = value.length;
		nextChar();
		while (j < i) {
			if (value[j] != ' ' || value[j] != '\t') {
				break;
			}
			++j;
		}
		if (i > 0 && j == i) {
			status = 2;
		}
		if (isSys() || i == l) {
			field = null;
			entity = null;
			status = 1;
		} else {
			while ((i < l) && (!isPunc()))
				++i;
			if (i == l) {
				field = null;
				while (!Character.isLetterOrDigit(value[l - 1]))
					--l;
				entity = new String(value, s, l - s);
			} else {
				j = i;
				while (!Character.isLetterOrDigit(value[j - 1]))
					--j;
				field = new String(value, s, j - s);
				nextChar();
				if (i == l) {
					entity = null;
				} else {
					s = i;
					while (!Character.isLetterOrDigit(value[l - 1]))
						--l;
					entity = new String(value, s, l - s);
				}
			}
		}

	}

	public String getField() {
		return field;
	}

	public String getValue() {
		return entity;
	}

	public int getStatus() {
		return status;
	}

	public void nextChar() {
		while ((i < l) && (!Character.isLetterOrDigit(value[i]))) {
			++i;
		}
	}

	public boolean isPunc() {
		if (value[i] == ':'
				&& (((i + 2 < l) && Character.isDigit(value[i - 1]) && Character.isDigit(value[i - 2])
						&& Character.isDigit(value[i + 1]) && Character.isDigit(value[i + 2])) || ((i + 1 < l) && value[i + 1] == '/'))) {
			return false;
		}
		if ((value[i] == '.') && (value[i - 1] == '.')) {
			return true;
		}
		if (value[i] == ' ' && value[i - 1] == '.' && status == 2) {
			return true;
		}
		return (value[i] == ':') || (value[i] == '\t') || (value[i] == ']') || (value[i] == '[');
	}

	public boolean isSys() {
		while (s < i) {
			if (value[s] == '#') {
				if (Character.isDigit(value[i])) {
					return false;
				} else {
					return true;
				}
			}
			if (value[s] == '>' || value[s] == ';' || value[s] == '%' || value[s] == '<') {
				return true;
			}
			++s;
		}
		return false;
	}
}