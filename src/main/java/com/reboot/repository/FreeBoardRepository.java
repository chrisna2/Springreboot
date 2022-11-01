package com.reboot.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.FreeBoard;

public interface FreeBoardRepository extends CrudRepository<FreeBoard, Long>{

	//현재 bno보다 큰 게시물의 목록을 조회 페이징 처리
	public List<FreeBoard> findByBnoGreaterThan(Long bno, Pageable page);
	
}
