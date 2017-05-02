package com.jdoa.tool;

import java.io.Serializable;

import org.apache.commons.codec.binary.Hex;

public final class JDUuid implements Serializable {
	private static String billType;
	private static String code;
	
	public JDUuid(String type){
		if (type != null && type.length() == 8) {
			billType = type;
			code = getRandomCode();
		}
	}
	
	private JDUuid(String type, String code0) {
		code = code0;
		billType = type;
	}

	public static String createID(String type) {
		String id = null;
		if (type != null && type.length() == 8) {
			code = getRandomCode();
			billType = type;
			id = code + billType;
		}
		return id;
	}

	public static JDUuid getJDUuid(String type) {
		JDUuid bpid = null;
		if (type != null && type.length() == 8) {
			code = getRandomCode();
			billType = type;
			bpid = new JDUuid(billType, code);
		}
		return bpid;
	}

	private static String getRandomCode() {
		return new String(Hex.encodeHex(org.apache.commons.id.uuid.UUID
				.randomUUID().getRawBytes()));
	}

	public String toString() {
		String id = null;
		if (billType != null && code != null) {
			id = code + billType;
		}

		return id;
	}
}
