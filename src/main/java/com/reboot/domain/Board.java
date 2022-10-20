package com.reboot.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity //JPA 데이터 명시
@Table(name="tbl_board")//JPA DB에 생성되는 테이블
public class Board {
	
	@Id//PK
	@GeneratedValue(strategy = GenerationType.AUTO)//PK 자동 생성 전략 : 데이터 베이스 특성에 맞게 자동 생성
	//@GeneratedValue(strategy = GenerationType.IDENTITY)//PK 자동 생성 전략 : 기본키 생성은 DB에 위임 - mysql, mariadb
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)//PK 자동 생성 전략 : DB 시퀀스를 통해 생성 - oracle
	//@GeneratedValue(strategy = GenerationType.TABLE)//PK 자동 생성 전략 : 별도의 채번 테이블을 활용하는 방식 
	private Long bno;//ID
	
	private String title;
	private String writer;
	private String content;
	
	@CreationTimestamp//insert시 생성
	private Timestamp regDate;		//localDateTime 
	
	@UpdateTimestamp//update시 생성
	private Timestamp updateDate;	//localDateTime 

}
