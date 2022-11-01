package com.reboot;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.reboot.domain.FreeBoard;
import com.reboot.domain.FreeBoardReply;
import com.reboot.repository.FreeBoardReplyRepository;
import com.reboot.repository.FreeBoardRepository;

import lombok.extern.java.Log;

@SpringBootTest
@Log
public class FreeBoardTest {

	@Autowired
	FreeBoardRepository boardRepo;
	
	@Autowired
	FreeBoardReplyRepository boardReplyRepo;
	
	//@Test
	public void insertDummyData() {
		
		IntStream.range(1, 200).forEach(i -> {
			FreeBoard board = new FreeBoard();
			
			board.setTitle("Free Board ... "+i);
			board.setContent("Free Content ... " +i);
			board.setWriter("User"+i%10);
			
			boardRepo.save(board);
		});
		
	}
	
	@Transactional
	@Commit
	//@Test
	public void insertReply2Way() {
		
		
		// [양방향 | 1] FreeBoard객체를 얻어온 후 
		Optional<FreeBoard> result = boardRepo.findById(199L);
		
		result.ifPresent(board -> {
			
			List<FreeBoardReply> replies = board.getReplies();
			
			// [양방향 | 2] FreeBoardReply를 
			FreeBoardReply reply = new FreeBoardReply();
			
			reply.setReply("Reply..................");
			reply.setReplyer("replyer00");
			reply.setBoard(board);
			// [양방향 | 2] FreeBoardReply를 댓글리스트에 추가한 후에
			replies.add(reply);
			
			// [1] 영속성 부여 처리 해야 됨 FreeBoard.java -> replies 관련 컬럼 CascadeType.ALL
			// [2] 트랜젝셔날 처리 : @Transactional 한번에 처리되는 부분을 중간에 에러나면 롤백처리..

			
			// [양방향 | 3] FreeBoard 자체를 저장하는 방식
			board.setReplies(replies);
			boardRepo.save(board);
 			
		});
		
		/*
		 양방향 입력의 경우 기존 board 테이블을 조회 하고 해당 밸류를 확인한뒤
		 해당 board에 입력할 replies를 insert처리 즉 트랜잭션안에 두가지 액션이 수행됨
		 그래서 @Transactional 처리가 필요		
		 
		 댓글 입력의 주체가 FreeBoard -> Reply
		 
			Hibernate: 
				select 
					freeboard0_.bno as bno1_2_0_, 
					freeboard0_.content as content2_2_0_, 
					freeboard0_.regdate as regdate3_2_0_, 
					freeboard0_.title as title4_2_0_, 
					freeboard0_.updatedate as updateda5_2_0_, 
					freeboard0_.writer as writer6_2_0_ 
				from tbl_freeboards freeboard0_ 
				where freeboard0_.bno=?
				
			Hibernate: 
				insert into tbl_free_replies 
					(board_bno, reply, replydate, replyer, updatedate) 
				values 
					(?, ?, ?, ?, ?)
		*/
	}
	
	@Transactional
	@Commit
	//@Test
	public void insertReply1Way() {
		
		// [단방향 | 1] FreeBoardReply를 생성하고
		FreeBoardReply reply = new FreeBoardReply();
		reply.setReply("Reply.........");
		reply.setReplyer("replyer00");
		
		// [단방향 | 2] FreeBoard는 자체는 새로 만들어 bno 속성만 지정하여 처리 하는 방식
		FreeBoard board = new FreeBoard();
		board.setBno(197L);
		
		reply.setBoard(board);
		
		boardReplyRepo.save(reply);
		
		//단방향 방식의 댓글을 추가 할 경우 insert 1번만 처리됨
		
		/*Hibernate: 
			
			입력의 주체가 reply 단일 처리 여기에 board에 해당하는 value를 reply에 셋팅하여 처리
			즉 이를 하기 위해선 reply 테이블에 board 값에 해당하는 값의 처리가 필요..
		 	
		 	insert into tbl_free_replies 
		 		(board_bno, reply, replydate, replyer, updatedate) 
		 	values 
		 		(?, ?, ?, ?, ?)
		*/
	}
	
	@Test
	public void selectBoardTest() {
		
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		
		
		/*
		Hibernate: 
			select 
				freeboard0_.bno as bno1_2_, 
				freeboard0_.content as content2_2_, 
				freeboard0_.regdate as regdate3_2_, 
				freeboard0_.title as title4_2_, 
				freeboard0_.updatedate as updateda5_2_, 
				freeboard0_.writer as writer6_2_ 
			from 
				tbl_freeboards freeboard0_ 
			where freeboard0_.bno>? 
			order by freeboard0_.bno desc limit ?
		*/
		
		boardRepo.findByBnoGreaterThan(0L, page).forEach(freeboard -> {
			System.out.println(freeboard);
		});
		
		
		
	}
	
	
	
}
