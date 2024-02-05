package com.project.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

    //중복된 회원있는지 체크할 쿼리 메소드 작성
    Member findByEmail(String email);
}