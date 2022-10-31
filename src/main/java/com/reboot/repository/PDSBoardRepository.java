package com.reboot.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.PDSBoard;

public interface PDSBoardRepository extends CrudRepository<PDSBoard, Long>{
	
	@Modifying //@Query에서는 select만 지원해서 처리한다. 
	//근데 수정및 입력을 해야되는 경우 해당 @Modifying annotaition을 추가 하면 됨 insert, update, delete 가능
	@Query("UPDATE FROM PDSFile f SET f.pdsfile = ?2 WHERE f.fno = ?1 ")
	public int updatePDSFile(Long fno, String newFileName);
	
	
	
	

}
