package com.reboot.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "member")//멤버는 제오 시킴
@Entity
@Table(name="tbl_profile")
@EqualsAndHashCode(of="fno")
public class Profile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fno;
	
	private String fname;
	
	private boolean current;
	
	/* 단방향 설정 프로핑에서 회원으로 접근만 가능 */
	@ManyToOne // 프로파일 Many : 멤버 One | 프로필 -> 멤버 
	private Member member;

}
