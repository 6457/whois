package thin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ThinParser {
	int tid;
	Calendar calendar;
	DateFormat df1;
	DateFormat df2;
	DateFormat df3;

	public ThinParser() {
		df1 = new SimpleDateFormat("dd-MMM-yyyy");
		df1.setTimeZone(TimeZone.getTimeZone("GMT"));
		calendar = df1.getCalendar();
		df2 = new SimpleDateFormat("yyyy-MM-dd");
		df2.setCalendar(calendar);
		df3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df3.setCalendar(calendar);
	}
	
	@SuppressWarnings("deprecation")
	public Date parseDate(String str) {
		if (str.matches("^\\d{4}-\\w{3}-\\d{2}$")) {
			try {
				return new Date(df1.parse(str).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (str.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			try {
				return new Date(df2.parse(str).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (str.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$")) {
			try {
				return new Date(df3.parse(str).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				return new Date(java.util.Date.parse(str));
			} catch (Exception e) {
				System.err.println("Failed to parse date: " + str);
			}
		}
		return null;
	}

	public ThinData parse(String whois) {
		ThinData td = new ThinData();
		try {
			String[] texts = whois.split("\\r?\\n");
			for (String text : texts) {
				if (text.length() == 0) {
					continue;
				}
				if (text.startsWith(">>> Last update of whois database:"))
					break;
				ThinTitleValue fe = new ThinTitleValue(text);
				String field = fe.getTitle();
				if (field == null) {
					continue;
				}
				switch (fe.getTitle()) {
				case "Domain Name":
					td.setDomain(fe.getValue());
					break;
				case "Registrar":
					td.setRegistrar(fe.getValue());
					break;
				case "Sponsoring Registrar IANA ID":
					td.setRegistrarID(Integer.parseInt(fe.getValue()));
					break;
				case "Whois Server":
					td.setWHOISServer(fe.getValue());
					break;
				case "Referral URL":
					td.setReferrelURL(fe.getValue());
					break;
				case "Name Server":
					td.addNameServer(fe.getValue());
					break;
				case "Status":
					td.addStatus(fe.getValue().split("\\s")[0]);
					break;
				case "Updated Date":
					td.setUpdate(parseDate(fe.getValue()));
					break;
				case "Creation Date":
					td.setCreate(parseDate(fe.getValue()));
					break;
				case "Expiration Date":
					td.setExpire(parseDate(fe.getValue()));
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return td;
	}
}