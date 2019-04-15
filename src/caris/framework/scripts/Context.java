package caris.framework.scripts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Context {

	private Map<String, String> stringContext;
	private Map<String, Integer> intContext;
	private Map<String, IUser> userContext;
	private Map<String, IRole> roleContext;
	
	public Context() {
		stringContext = new HashMap<String, String>();
		intContext = new HashMap<String, Integer>();
		userContext = new HashMap<String, IUser>();
		roleContext = new HashMap<String, IRole>();
	}
	
	public Context(Context context) {
		this();
		for( String stringKey : context.getStringKeys() ) {
			putString(stringKey, context.getString(stringKey));
		}
		for( String intKey : context.getIntKeys() ) {
			putInt(intKey, context.getInt(intKey));
		}
		for( String userKey : context.getUserKeys() ) {
			putUser(userKey, context.getUser(userKey));
		}
		for( String roleKey : context.getRoleKeys() ) {
			putRole(roleKey, context.getRole(roleKey));
		}
	}
	
	public boolean hasString(String key) {
		return stringContext.containsKey(key);
	}
	
	public boolean hasInt(String key) {
		return intContext.containsKey(key);
	}
	
	public boolean hasUser(String key) {
		return userContext.containsKey(key);
	}
	
	public boolean hasRole(String key) {
		return roleContext.containsKey(key);
	}
	
	public String getString(String key) {
		return stringContext.get(key);
	}
	
	public int getInt(String key) {
		return intContext.get(key);
	}
	
	public IUser getUser(String key) {
		return userContext.get(key);
	}
	
	public IRole getRole(String key) {
		return roleContext.get(key);
	}
	
	public Set<String> getStringKeys() {
		Set<String> stringKeys = new HashSet<String>();
		for( String key : stringContext.keySet() ) {
			stringKeys.add(key);
		}
		return stringKeys;
	}
	
	public Set<String> getIntKeys() {
		Set<String> intKeys = new HashSet<String>();
		for( String key : intContext.keySet() ) {
			intKeys.add(key);
		}
		return intKeys;
	}
	
	public Set<String> getUserKeys() {
		Set<String> userKeys = new HashSet<String>();
		for( String key : userContext.keySet() ) {
			userKeys.add(key);
		}
		return userKeys;
	}
	
	public Set<String> getRoleKeys() {
		Set<String> roleKeys = new HashSet<String>();
		for( String key : roleContext.keySet() ) {
			roleKeys.add(key);
		}
		return roleKeys;
	}
	
	public void putString(String key, String value) {
		intContext.remove(key);
		userContext.remove(key);
		roleContext.remove(key);
		
		stringContext.put(key, value);
	}
	
	public void putInt(String key, int value) {
		stringContext.remove(key);
		userContext.remove(key);
		roleContext.remove(key);
		
		intContext.put(key, value);
	}
	
	public void putUser(String key, IUser value) {
		stringContext.remove(key);
		intContext.remove(key);
		roleContext.remove(key);
		
		userContext.put(key, value);
	}
	
	public void putRole(String key, IRole value) {
		stringContext.remove(key);
		intContext.remove(key);
		userContext.remove(key);
		
		roleContext.put(key, value);
	}
	
}
