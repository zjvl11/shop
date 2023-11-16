package com.project.shop.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
public class QuerydslConfig {

	@PersistenceContext
	private EntityManager entityManager;
	
	//JPAQueryFactory 를 Bean 으로 등록해서 프로젝트 전역에서 사용할 수 있도록 한다
	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		
		return new JPAQueryFactory(entityManager);
	}
	
}
