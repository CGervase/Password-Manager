package application;

import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class User {
	private String userID;
	private Password masterPass;
	private Map<String, Password> passMap = new TreeMap<String, Password>();

	public User(String id, String pass) {
		userID = id;
		masterPass = new Password(pass);
	}

	public String getID() {
		return userID;
	}

	public Password getPass() {
		return masterPass;
	}

	public boolean validatePass(String pass) {
		return pass.equals(masterPass.toString());
	}

	public List<String> keyList() {
		List<String> keys = new LinkedList<String>(passMap.keySet());
		
		if (keys.size() > 0 && keys.get(0).isEmpty())
			keys.remove(0);
		return keys;
	}

	public Password getPass(String key) {
		return passMap.get(key);
	}

	public void addPass(String key, String pass) {
		passMap.put(key, new Password(pass));
	}

	public void deletePass(String key) {
		passMap.remove(key);
	}

	public boolean hasKey(String key) {
		return passMap.containsKey(key);
	}
}