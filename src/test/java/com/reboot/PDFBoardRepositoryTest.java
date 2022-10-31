package com.reboot;

import java.util.Arrays;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.reboot.domain.PDSBoard;
import com.reboot.domain.PDSFile;
import com.reboot.repository.PDFBoardRepository;

@SpringBootTest // 2.5.x 버전 이후 @RunWith은 사라지고 @SpringBootTest에 포함됨
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PDFBoardRepositoryTest {
	
	@Autowired
	PDFBoardRepository repo;
	
	
	//@Test	//파일 등록
	public void testInsertPDS() {
		PDSBoard pds = new PDSBoard();
		pds.setPname("Document");
		pds.setPwriter("deft");
		
		PDSFile pfile1 = new PDSFile();
		pfile1.setPdsfile("file1.doc");
		
		PDSFile pfile2= new PDSFile();
		pfile2.setPdsfile("file2.doc");
		
		pds.setFiles(Arrays.asList(pfile1,pfile2));
		
		System.out.println("try to save pds");
		/*
		try to save pds
			//부모 테이블
			Hibernate: insert into tbl_pds (pname, pwriter) values (?, ?)
			//자식 테이블 
			Hibernate: insert into tbl_pds_files (pdsfile) values (?)
			Hibernate: insert into tbl_pds_files (pdsfile) values (?)
			Hibernate: update tbl_pds_files set pdsno=? where fno=?
			Hibernate: update tbl_pds_files set pdsno=? where fno=?
		*/
		repo.save(pds);
	}
	
}
