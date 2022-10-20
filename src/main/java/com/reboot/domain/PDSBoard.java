package com.reboot.domain;

import java.util.List;

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
	
	@OneToMany // 자료 -> 첩부파일 | 자료 : 1 - 첨부파일 : Many 
	@JoinColumn(name="pdsno") //pdsno라는 신규 컬럼이 PDSFile에 생성되고 이는 pdsfile의 fno가 들어 가게 된다. //이걸 안하면 의도와 다르게 연관 테이블이 또 생성되게 됨
	private List<PDSFile> files;

}
