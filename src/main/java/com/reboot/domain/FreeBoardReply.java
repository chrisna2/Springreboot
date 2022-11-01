package com.reboot.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "board") //이 처리를 해주지 않으면 끊임없이 해당 밸류에 대한 값을 조회하기 때문에 해당 값에 대한 처리는 exclude처리
@Entity
@Table(name = "tbl_free_replies")
@EqualsAndHashCode(of = "rno")
public class FreeBoardReply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rno;
	
	private String reply;
	
	private String replyer;
	
	@CreationTimestamp
	private Timestamp replydate;
	
	@UpdateTimestamp
	private Timestamp updatedate;
	
	// 연관관계의 설정 
	// 게시물과 댓글의 관계 : 일대다 - 일대다 - 단방향 : **게시물(1)** <-> **댓글(Many)** 
	// 양 방향 관계 이기 때문에 두개의 entity 흘러 가는 엔티티에 대한 설정을 처리해 줘야 한다.
	// 일대다 관계에 대한 설정 : 지금의 엔티티가 Many인경우 상대에 대한 결과를 단건의 오브젝트로 리턴 받는 것을 설정해 준다.
	// 어노테이션은 @ManyToOne 지금의 엔티티의 상태가 Many 일때 Many가 우선하고 그 다음 상대(One)가 따라온다.
	@ManyToOne
	private FreeBoard board;
	
}
