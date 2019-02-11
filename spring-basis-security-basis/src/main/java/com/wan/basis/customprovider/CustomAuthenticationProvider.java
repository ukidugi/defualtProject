package com.wan.basis.customprovider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.wan.basis.CustomUserDetailsService;
import com.wan.basis.dto.user;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider { //authenticationManager
	
	@Autowired
	private CustomUserDetailsService customeUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication; //유저정보와 이이디비번으으로만든다.(로그인한 유저아이디비번정보를담는다)

		user userInfo = customeUserDetailsService.loadUserByUsername(authToken.getName()); //UserDetailsService에서 유저정보를 불러온다.
		if (userInfo == null) {
			throw new UsernameNotFoundException(authToken.getName());
		}

		// userDetailservice에서는 디비의 유저정보를 돌려주고 (순수 그냥 말그대로 유저정보만 돌려주는거임)
		//이클래스는 인증이된 유저정보를 돌려준다.
		
		if (!matchPassword(userInfo.getPassword(), authToken.getCredentials())) {  //내일 xml에 이파일들을 넣고 여기에서 암호화 대조 authToken이 뭔지알아내야함 
			throw new BadCredentialsException("not matching username or password");
		}

		List<GrantedAuthority> authorities = (List<GrantedAuthority>) userInfo.getAuthorities(); // 유저마다 가진권한을 읽어와야하기떄문에 
//		return new UsernamePasswordAuthenticationToken(									//여기에서 유저아이디로디비에서 권한가지고와도괜찮을
//				new UserInfo(userInfo.getEnabled(), userInfo.getUsername(), null),
//				null,
//				authorities); 
		
		return new UsernamePasswordAuthenticationToken(userInfo,null,authorities);
	}

	private boolean matchPassword(String password, Object credentials) {
		return password.equals(credentials);
	}
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
