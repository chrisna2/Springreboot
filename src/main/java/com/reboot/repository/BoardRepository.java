package com.reboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long>{
	
	//인터페이스 상속 구조 
	//Repository (기능없음) > CrudRepository (CRUD) > PagingAndSortingRepository (페이징 기능 추가) > JpaRepository (JPA에 특화된 기능 추가)
	
	
	

}
