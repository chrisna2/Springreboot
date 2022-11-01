package com.reboot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.reboot.domain.PDSBoard;
import com.reboot.domain.PDSFile;
import com.reboot.repository.PDSBoardRepository;

@SpringBootTest // 2.5.x 버전 이후 @RunWith은 사라지고 @SpringBootTest에 포함됨
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PDSBoardRepositoryTest {
	
	@Autowired
	PDSBoardRepository repo;
	
	
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
	
	//@Test
	//@Transactional //org.springframework.transaction.annotation.Transactional
	//TEST 코드에서 해당 annotation을 사용할 경우 해당 트랜잭션의 처리 결과를 롤백처리 한다. 그래서 결과가 commit되지 않는 것.
	//@Commit //org.springframework.test.annotation.Commit; 이걸 추가해 줘야 해당 쿼리의 데이터가 변경된다.
	public void updatePdsFileTest() {
		
		Long fno = 1L;
		String newName = "updatedFile1.doc";
		
		int count = repo.updatePDSFile(fno, newName);
		
		System.out.println("update Count : "+count);
		
		/* 
		//이 경우 해당 repository가 PDSBoard 를 대상으로 하기 때문에 PDSBoard 엔티티의 데이터를 조회한다.
		repo.findById(1L).ifPresent(pds -> {
			System.out.println(pds);
		});
		*/
	}
	
	//@Test
	//@Transactional //org.springframework.transaction.annotation.Transactional
	//TEST 코드에서 해당 annotation을 사용할 경우 해당 트랜잭션의 처리 결과를 롤백처리 한다. 그래서 결과가 commit되지 않는 것.
	//@Commit //org.springframework.test.annotation.Commit; 이걸 추가해 줘야 해당 쿼리의 데이터가 변경된다.
	public void testUpdateFileName2() {
		
		String newName = "updatedFile2.doc";
		
		Optional<PDSBoard> result = repo.findById(2L);
		//Hibernate: select pdsboard0_.pid as pid1_2_0_, pdsboard0_.pname as pname2_2_0_, pdsboard0_.pwriter as pwriter3_2_0_ from tbl_pds pdsboard0_ where pdsboard0_.pid=?
		
		result.ifPresent(pds -> {
			
			System.out.println("데이터가 존제 함, Update 시작");
			
			PDSFile target = new PDSFile();
			
			target.setFno(2L); // 두번째 파일 수정
			target.setPdsfile(newName);
			
			//[중요] PDSFile이 데이터는  해당 데이터는 이미 repo.findById(2L)을 통해 이미 가져온 상태임!
			
			int idx = pds.getFiles().indexOf(target);
			
			if(idx > -1) {
				List<PDSFile> list = pds.getFiles();
				
				list.remove(idx);
				list.add(target);
				
				pds.setFiles(list);
			}
			
			/*
			 Hibernate: 
			 	select 
			 		files0_.pdsno as pdsno3_3_0_, 
			 		files0_.fno as fno1_3_0_, 
			 		files0_.fno as fno1_3_1_, 
			 		files0_.pdsfile as pdsfile2_3_1_ 
			 	from tbl_pds_files files0_ 
			 	where files0_.pdsno=?
			
			Hibernate: 
				update 
					tbl_pds_files 
				set pdsfile=? 
				where fno=?	
			*/
			repo.save(pds);
			
		});
		
	}
	
	//@Test
	//@Transactional
	public void testDeleteFile() {
		
		Long fno = 2L;
		
		int count = repo.deletePDSFile(fno);
		
		System.out.println("Delete Count -> "+count);
		
	}
	
	//@Test
	public void insertDummies() {
		
		List<PDSBoard> list = new ArrayList<>();
		
		IntStream.range(1, 100).forEach(i -> {
			PDSBoard pds = new PDSBoard();
			pds.setPname("자료 "+i);
			
			PDSFile file1 =  new PDSFile();
			file1.setPdsfile("file1.doc");
			
			PDSFile file2 =  new PDSFile();
			file2.setPdsfile("file2.doc");
			
			pds.setFiles(Arrays.asList(file1,file2));
			
			System.out.println("try to save pds");
			
			list.add(pds);
			
		});
		//한번에 데이터를 처리하게 되면 한 번에 여러개의 데이터를 입력 가능.
		repo.saveAll(list);
		
	}
	
	
	@Test
	public void countPdsDataSumTest() {
		repo.getSummery().forEach(arr -> {
			System.out.println(Arrays.toString(arr));
		});
		
	}
	
	
}
