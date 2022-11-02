package com.reboot.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "replies") //이 처리를 해주지 않으면 끊임없이 해당 밸류에 대한 값을 조회하기 때문에 해당 값에 대한 처리는 exclude처리

@Entity
@Table(name = "tbl_freeboards")
@EqualsAndHashCode(of = "bno")
public class FreeBoard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;

	private String title;
	
	private String writer;
	
	private String content;
	
	@CreationTimestamp
	private Timestamp regdate;
	
	@UpdateTimestamp
	private Timestamp updatedate;
	
	
	// 연관관계의 설정 
	// 게시물과 댓글의 관계 : 일대다 - 일대다 - 단방향 : **게시물(1)** <-> **댓글(Many)** 
	// 양 방향 관계 이기 때문에 두개의 entity 흘러 가는 엔티티에 대한 설정을 처리해 줘야 한다.
	// 일대다 관계에 대한 설정 : 지금의 엔티티가 1인경우 상대에 대한 결과를 **리스트**로 리턴 받는 것을 설정해 준다.
	// 어노테이션은 @OneToMany 지금의 엔티티의 상태가 One 일때 One이 우선하고 그 다음 상대(Many)가 따라온다.
	@OneToMany(mappedBy = "board", 			// 게시글이 댓글에 매어있는 상태
			   cascade = CascadeType.ALL, 	// 영속성 전의 : 부모테이블의 모든 변경(CRUD)에 대하여 자식테이블의 전의
			   fetch = FetchType.LAZY) 		// 로딩방식 : 즉시로딩 설정(FetchType.EAGER), 지연로딩 설정(FetchType.LAZY : 기본값)
	private List<FreeBoardReply> replies;
	
	// 해당 연관 관계에 따라 tbl_free_replies 테이블에는 board_bno라는 fk가 생성되는 것으로 확인이 가능 하다.
	
	/* Mappedby
	- 자신이 다른 객체에 "메어있다"는 것을 명시
	- 해당 엔티티가 관계의 주체가 되지 않는다.
	- 테이블이 데이터흫 삭제하는 처리를 예제 : 댓글이 있는 상태에서 계시글의 삭제가 불가능함 -> 게시글이 댓글에 매어 있다.
	Hibernate: create table tbl_free_replies (rno bigint not null auto_increment, reply varchar(255), replydate datetime(6), replyer varchar(255), updatedate datetime(6), board_bno bigint, primary key (rno)) engine=InnoDB
	Hibernate: create table tbl_freeboards (bno bigint not null auto_increment, content varchar(255), regdate datetime(6), title varchar(255), updatedate datetime(6), writer varchar(255), primary key (bno)) engine=InnoDB
	Hibernate: alter table tbl_free_replies add constraint FKibfuflxivfvwvdhk1gmwfafvg foreign key (board_bno) references tbl_freeboards (bno)
	*/
}
