package com.project.shop.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.project.shop.domain.MemberFormDTO;
import com.project.shop.entity.Member;
import com.project.shop.service.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Member createMember(String email, String password) {
		MemberFormDTO memberFormDto = new MemberFormDTO();
		memberFormDto.setId("abcd1");
		memberFormDto.setPassword("1234");
		memberFormDto.setName("김철수");
		memberFormDto.setEmail("abcd1@naver.com");
		memberFormDto.setGender("m");
		memberFormDto.setMobile("010-1234-1234");
		memberFormDto.setPhone("02-777-8888");
		memberFormDto.setZip("08290");
		memberFormDto.setAddress1("서울 구로구 공원로 83 (구로동)");
		memberFormDto.setAddress2("이젠아카데미 신도림점");
		
		Member member = Member.createMember(memberFormDto, passwordEncoder);
		return memberService.saveMember(member);
	}
	
	@Test
	@DisplayName("로그인 성공 테스트")
	public void loginSuccessTest() throws Exception{
		String email = "test@email.com";
		String password = "1234";
		this.createMember(email,  password);
		
		mockMvc.perform(formLogin().userParameter("email")
			   .loginProcessingUrl("/members/login")
			   .user(email).password(password))
			   .andExpect(SecurityMockMvcResultMatchers.authenticated());
	}
	
	@Test
	@DisplayName("로그인 실패 테스트")
	public void loginFailTest() throws Exception{
		String email = "test@email.com";
		String password = "1234";
		this.createMember(email,  password);
		
		mockMvc.perform(formLogin().userParameter("email")
			   .loginProcessingUrl("/members/login")
			   .user(email).password("12345"))
			   .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
	}
	
	
	
}
