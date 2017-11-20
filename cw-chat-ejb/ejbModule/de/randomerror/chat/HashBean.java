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
	public void init() throws NoSuchAlgorithmException {
		encoder = MessageDigest.getInstance(algorithm);
	}
	
	
	public String hash(String str) {
		return String.format("", new BigInteger(1, encoder.digest(str.getBytes())));
	}

}
