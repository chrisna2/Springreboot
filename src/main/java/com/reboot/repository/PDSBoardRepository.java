package com.reboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.PDSBoard;

public interface PDSBoardRepository extends CrudRepository<PDSBoard, Long>{
	
	@Modifying //@Query에서는 select만 지원해서 처리한다. 
	//근데 수정및 입력을 해야되는 경우 해당 @Modifying annotaition을 추가 하면 됨 insert, update, delete 가능
	@Query("UPDATE FROM PDSFile f SET f.pdsfile = ?2 WHERE f.fno = ?1 ")
	public int updatePDSFile(Long fno, String newFileName);
	
	
	@Modifying
	@Query("DELETE FROM PDSFile f WHERE f.fno = ?1")
	public int deletePDSFile(Long fno);
	
	//자료와 첨부파일의 수를 자료 번호의 역순으로 출력
	/*
		SELECT 
			A.*,
			(SELECT COUNT(*) FROM TBL_PDS_FILES B WHERE A.PID = B.PDSNO) AS COUNT
		FROM 
			TBL_PDS A
		ORDER BY A.PID DESC;




	@Query("SELECT a, "
			+ "(SELECT COUNT(b) "
			+ "FROM PDSFile b "
			+ "WHERE a.pid = b.pdsno) AS COUNT "
			+ "FROM PDSBoard a "
			+ "ORDER BY a.pid DESC")
			
	 */
	
	/*
	@Query("SELECT p, count(f) "
			+ "FROM PDSBoard p "
				+ "LEFT OUTER JOIN p.files f "
			+ "WHERE p.pid > 0 "
			+ "GROUP BY p "
			+ "ORDER BY pid DESC ")
	*/
	
	@Query(value = 	
		"SELECT p.pid, p.pname, p.pwriter, COUNT(f.fno) "+ 
		"FROM tbl_pds p "+ 
			"LEFT OUTER JOIN tbl_pdsfiles f "+ 
			"ON f.pdsno = p.pid "+ 
		"WHERE p.pid > 0 "+
		"GROUP BY p.pid "+
		"ORDER BY p.pid DESC" , nativeQuery = true)
	public List<Object[]> getSummery();
	
	/* 
		@Query("SELECT p, count(f) "
				+ "FROM PDSBoard p "
					+ "LEFT OUTER JOIN p.files f "
				+ "WHERE p.pid > 0 "
				+ "GROUP BY p "
				+ "ORDER BY pid DESC ")
	
		 --------------------------------------------
		 
		 select 
		 	pdsboard0_.pid as col_0_0_, 
		 	count(files1_.fno) as col_1_0_, 
		 	pdsboard0_.pid as pid1_2_, 
		 	pdsboard0_.pname as pname2_2_, 
		 	pdsboard0_.pwriter as pwriter3_2_ 
		 from tbl_pds pdsboard0_ 
		 	left outer join tbl_pds_files files1_ 
		 	on pdsboard0_.pid=files1_.pdsno 
		 where pdsboard0_.pid>0 
		 group by pdsboard0_.pid 
		 order by pid DESC
		 
		 --------------------------------------
		 
		SELECT p.*, COUNT(f.fno) 
		FROM TBL_PDS p LEFT 
			OUTER JOIN tbl_pds_files f 
			ON f.pdsno = p.pid
		WHERE p.pid > 0 
		GROUP BY p.pid
		ORDER BY p.pid DESC ;
		
		-> 이걸 사용하게 될 경우. nativeQuery 옵션 실행
		
		@Query(value = 	
		"SELECT p.pid, p.pname, p.pwriter, COUNT(f.fno) "+ 
		"FROM tbl_pds p "+ 
			"LEFT OUTER JOIN tbl_pds_files f "+ 
			"ON f.pdsno = p.pid "+ 
		"WHERE p.pid > 0 "+
		"GROUP BY p.pid "+
		"ORDER BY p.pid DESC" , nativeQuery = true)
				
		-----------------------------------------
		
		
		hibernate 쿼리와
		
		hibernate 쿼리로 변경한 실제 쿼리는
		
		문법이 다르다. 이걸 확인해야 된다.
		 
	 */

}
