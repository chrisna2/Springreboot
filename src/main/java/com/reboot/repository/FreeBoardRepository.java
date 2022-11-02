package com.reboot.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.FreeBoard;

public interface FreeBoardRepository extends CrudRepository<FreeBoard, Long>{

	//현재 bno보다 큰 게시물의 목록을 조회 페이징 처리
	public List<FreeBoard> findByBnoGreaterThan(Long bno, Pageable page);
	
	
	//무조건 조인 관계를 명시해 줄것
	@Query("SELECT "
			+ "fb.bno, "
			+ "fb.title, "
			+ "fb.content, "
			+ "count(fbr) "
			+ "FROM FreeBoard fb LEFT OUTER JOIN fb.replies fbr "
			+ "WHERE fb.bno > 0 "
			+ "GROUP BY fb")
	public List<Object[]> getFreeBoardPageList(Pageable page);
	
}
