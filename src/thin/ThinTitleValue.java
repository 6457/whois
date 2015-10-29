package thin;

public class ThinTitleValue {
	String title = null;
	String entity = null;

	public ThinTitleValue(String item) {
		char[] value = item.toCharArray();
		int l = value.length;
		int i = 0;
		while ((i < l) && !Character.isLetterOrDigit(value[i]))
			++i;
		if (i == l)
			return;
		int h = i;
		while ((i < l) && value[i] != ':')
			++i;
		if (i == l)
			return;
		int t = i;
		while (!Character.isLetterOrDigit(value[t - 1]))
			--t;
		title = new String(value, h, t - h);
		while ((i < l) && !Character.isLetterOrDigit(value[i]))
			++i;
		if (i == l)
			return;
		h = i;
		t = l;
		while (!Character.isLetterOrDigit(value[t - 1]))
			--t;
		entity = new String(value, h, t - h);
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getValue() {
		return entity;
	}
}