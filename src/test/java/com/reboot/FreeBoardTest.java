package com.reboot;

import java.util.Arrays;
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

@SpringBootTest
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
	@Test
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
	@Test
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
	
	//@Test
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
	
	////@Transactional
	//@Test
	public void testList2() {
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		
		boardRepo.findByBnoGreaterThan(1L, page).forEach(board -> {
			
			//굳이 조인 걸지 않아도 board객체 안에 내포 되어 있는 reply 리스트의 사이즈를 구하면  카운팅을 처리 할 수 있다.
			System.out.println(board.getBno()+":"+board.getTitle()+":"+board.getReplies().size());
			
			/*
				[조건] JPA는 연관관계가 있는 엔티티를 조회할 때 기본적으로 지연로딩을 사용한다. 
				- 지연로딩 : "게으른"이라는 의미로 정보가 필요하기 전까지는 최대한 테이블에 접근하지 않는 방식
					> 정보가 필요하기 전까지 최대한 테이블에 접근하지 않는다.
					> 지연로딩을 사용하는 이유 : 성능상의 이슈, 하나의 엔티티가 여러 엔티티들과 종속적인 관계를 맺고 있다면 
					SQL의 경우 조인을 사용하지만, 조인이 복잡해지면 성능이 저하된다. 
					따라서 JPA는 연관관계의 Collerction을 사용하는 경우 지연로딩을 기본적으로 사용한다.
				[사용법] : 기본 값 처리, 테스트에 해당 로직의 작동을 보고 싶으면 @Transactional 추가
				[결과] : 
				<페이지 목록으로 처리되는 부분 sql 처리>				
					Hibernate: select freeboard0_.bno as bno1_2_, freeboard0_.content as content2_2_, freeboard0_.regdate as regdate3_2_, freeboard0_.title as title4_2_, freeboard0_.updatedate as updateda5_2_, freeboard0_.writer as writer6_2_ from tbl_freeboards freeboard0_ where freeboard0_.bno>? order by freeboard0_.bno desc limit ?
				<이후 각 게시물에 포함된 reply 목록 조회 * 10>
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					199:Free Board ... 199:1
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					198:Free Board ... 198:1
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					197:Free Board ... 197:1
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					196:Free Board ... 196:0
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					195:Free Board ... 195:0
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					194:Free Board ... 194:0
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					193:Free Board ... 193:0
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					192:Free Board ... 192:0
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					191:Free Board ... 191:0
					Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
					190:Free Board ... 190:0
				
				- 즉시로딩 : 지연로딩의 반대 개념, 일반적으로 조인을 이용해서 필요한 모든 정보를 처리하게된다.
				[사용법] : 해당 엔티티의 연관관계 설정부 @ManytoOne 어노테이션에 fetch 속성 추가. 
				[결과] : **권장되지 않는 방법** .... 성능상의 이슈 발생
				
				<페이지 목록으로 처리되는 부분 sql 처리>
				Hibernate: 
						select 
							freeboard0_.bno as bno1_2_, 
							freeboard0_.content as content2_2_, 
							freeboard0_.regdate as regdate3_2_, 
							freeboard0_.title as title4_2_, 
							freeboard0_.updatedate as updateda5_2_, 
							freeboard0_.writer as writer6_2_ 
						from tbl_freeboards freeboard0_ 
						where freeboard0_.bno>? 
						order by 
							freeboard0_.bno desc 
						limit ?
				
				<이후 각 게시물에 포함된 reply 목록 조회 * 10>
				Hibernate: 
						select 
							replies0_.board_bno as board_bn6_1_0_, 
							replies0_.rno as rno1_1_0_, 
							replies0_.rno as rno1_1_1_, 
							replies0_.board_bno as board_bn6_1_1_, 
							replies0_.reply as reply2_1_1_, 
							replies0_.replydate as replydat3_1_1_, 
							replies0_.replyer as replyer4_1_1_, 
							replies0_.updatedate as updateda5_1_1_ 
						from tbl_free_replies replies0_ 
						where replies0_.board_bno=?
				
				<이하 반복> X 10 ... 
				
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.reply as reply2_1_1_, replies0_.replydate as replydat3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_free_replies replies0_ where replies0_.board_bno=?
				
				<성능상의 이슈 발생.....>
				
				### 즉, JPA도 리포지토리에 해당 두 테이블 간에 조인 조건을 명시해 주지 않으면 지연로딩을 하든 즉시로딩을 하든 
				성능상에 이슈가 발생할 수 밖에 없다...
		
			*/
		});
	}
	
	//@Test
	public void getFreeBoardPageListTest() {
		
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		
		boardRepo.getFreeBoardPageList(page).forEach(arr -> {
			System.out.println(Arrays.toString(arr));
		});
		
		/*
		 [결과] 
		 Hibernate: 
		 	select 
		 		freeboard0_.bno as col_0_0_, 
		 		freeboard0_.title as col_1_0_, 
		 		freeboard0_.content as col_2_0_, 
		 		count(replies1_.rno) as col_3_0_ 
		 	-------------------------------------------------
		 	from tbl_freeboards freeboard0_ 
		 		left outer join tbl_free_replies replies1_ 
		 		on freeboard0_.bno=replies1_.board_bno 
		 		(조인 관계 명시 필수)
		 	[JPA : FROM FreeBoard fb LEFT OUTER JOIN fb.replies fbr]
		 	--------------------------------------------------	
		 	where freeboard0_.bno>0 
		 	group by freeboard0_.bno 
		 	order by freeboard0_.bno desc 
		 	limit ?
		 */
	}
	
	
}
