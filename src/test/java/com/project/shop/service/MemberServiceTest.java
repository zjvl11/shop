package com.project.shop.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.project.shop.dto.MemberFormDto;
import com.project.shop.entity.Member;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setId("abcd1");
        memberFormDto.setPassword("1234");
        memberFormDto.setName("김철수");
        memberFormDto.setEmail("abcd1@naver.com");
//        memberFormDto.setGender("m");
//        memberFormDto.setMobile("010-1234-1234");
//        memberFormDto.setPhone("02-777-8888");
//        memberFormDto.setZip("08290");
//        memberFormDto.setAddress1("서울 구로구 공원로 83 (구로동)");
//        memberFormDto.setAddress2("이젠아카데미 신도림점");
//		memberFormDto.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-30"));

        return Member.createMember(memberFormDto, passwordEncoder);
    }


    @Test
    @DisplayName("회원가입 테스트")
    public void saveMeberTest() {
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getId(), savedMember.getId());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getEmail(), savedMember.getEmail());
//        assertEquals(member.getGender(), savedMember.getGender());
//        assertEquals(member.getMobile(), savedMember.getMobile());
//        assertEquals(member.getPhone(), savedMember.getPhone());
//        assertEquals(member.getZip(), savedMember.getZip());
//        assertEquals(member.getAddress1(), savedMember.getAddress1());
//        assertEquals(member.getAddress2(), savedMember.getAddress2());
//		assertEquals(member.getBirthday(), savedMember.getBirthday());
    }


    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest()  {

        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {memberService.saveMember(member2);});

        assertEquals("이미 가입된 회원입니다.", e.getMessage());

    }

}