package com.reboot.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.FreeBoard;
import com.reboot.domain.FreeBoardReply;

public interface FreeBoardReplyRepository extends CrudRepository<FreeBoardReply, Long>{
	
	
}
