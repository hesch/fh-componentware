package de.randomerror.chat;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Stateless;

@Stateless
@Startup
public class HashBean {
	
	@Resource(name="hash-algorithm")
	private String algorithm;
	
	private MessageDigest encoder;
	
	@PostConstruct
	public void init() {
		try {
			encoder = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	
	public String hash(String str) {
		return String.format("%040x", new BigInteger(1, encoder.digest(str.getBytes())));
	}

}
