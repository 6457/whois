package thin;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ThinData {
	String domain;
	String rar;
	Integer rarID;
	String wServer;
	String refer;
	List<String> nServer;
	List<String> status;
	Date update;
	Date create;
	Date expire;
	
	public ThinData() {
		domain = null;
		rar = null;
		rarID = null;
		wServer = null;
		refer = null;
		nServer = new LinkedList<>();
		status = new LinkedList<>();
		update = null;
		create = null;
		expire = null;
	}
	
	public String setDomain(String domainName) {
		return domain = domainName;
	}
	
	public String setRegistrar(String registrar) {
		return rar = registrar;
	}
	
	public Integer setRegistrarID(int id) {
		return rarID = id;
	}
	
	public String setWHOISServer(String server) {
		return wServer = server;
	}
	
	public String setReferrelURL(String url) {
		return refer = url;
	}
	
	public boolean addNameServer(String server) {
		return nServer.add(server);
	}
	
	public boolean addStatus(String domainStatus) {
		return status.add(domainStatus);
	}
	
	public Date setCreate(Date date) {
		return create = date;
	}
	
	public Date setUpdate(Date date) {
		return update = date;
	}
	
	public Date setExpire(Date date) {
		return expire = date;
		
	}
}