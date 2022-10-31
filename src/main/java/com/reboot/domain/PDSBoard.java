package com.reboot.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "files")
@Entity
@Table(name="tbl_pds")
@EqualsAndHashCode(of="pid")
public class PDSBoard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pid;
	
	private String pname;
	
	private String pwriter;
	
	@OneToMany(cascade = CascadeType.ALL)
	// 자료 -> 첩부파일 | 자료 : 1 - 첨부파일 : Many 
	// 영속성 전의 : 처리하려는 엔티티 객체의 상태에 따라서 종속적인 객체들의 영속성도 같이 처리되는 것
	// 예시) 날짜와 일정의 관계 : 특정한 날짜가 데이터 베이스에서 사라지면 해당 날짜에 속하는 일정도 같이 사라져야 한다.
	// jpa에서 엔티티들의 기본적으로 메모리 상의 관계 날짜객체가 사라지면 일정객체도 같이 삭제될 필요가 있음
	// 부모 앤티티나 자식 엔티티의 상태 변화가 자신과 관련있는 엔티티에 영향을 주는 것을 의미
	@JoinColumn(name="pdsno") 
	//pdsno라는 신규 컬럼이 PDSFile에 생성되고 이는 pdsfile의 fno가 들어 가게 된다. 
	//이걸 안하면 의도와 다르게 연관 테이블이 또 생성되게 됨
	private List<PDSFile> files; //List<PDSFile> 인건 여러개의 파일이 들어 갈수 있기 때문..

}
