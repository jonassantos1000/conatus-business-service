package br.com.app.conatus.util;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtil {
	
	public static String replaceBearer(String token) {
		if(StringUtils.isBlank(token)) {
			return token;
		}
		
		return token.replace("Bearer ", "");
	}

}
