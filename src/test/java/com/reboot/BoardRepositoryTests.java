package com.reboot;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.reboot.domain.Board;
import com.reboot.repository.BoardRepository;

@SpringBootTest // 2.5.x 버전 이후 @RunWith은 사라지고 @SpringBootTest에 포함됨
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardRepositoryTests {
	
	@Autowired
	private BoardRepository boardRepo;
	
	public static Long ID;	
	
	//@Test
	@Order(1)
	public void inspect() {
		//실제 객체의 클래스 이름
		Class<?> clz = boardRepo.getClass();
		
		System.out.println(clz.getName());
		
		//클래스가 구현하고 있는 인터페이스 목록
		Class<?>[] interfaces = clz.getInterfaces();
		
		//스트림 처리 ->인터페이스 목록 호출
		Stream.of(interfaces).forEach(inter -> System.out.println(inter.getName()));
		
		//클래스의 부모 클래스
		Class<?> superClasses = clz.getSuperclass();
		
		System.out.println(superClasses.getName());
		
	}
	
	//@Test
	@Order(2)
	public void testInsert() {
		
		Board board = new Board();
		
		board.setTitle("게시물의 제몽");
		board.setContent("게시물의 내용 .....");
		board.setWriter("사용자1");
		
		boardRepo.save(board);
		
		ArrayList<Long> idarray = new ArrayList();
		
		boardRepo.findAll().forEach( eachData -> {
			System.out.println(eachData);
			idarray.add(eachData.getBno());
		});
		
		BoardRepositoryTests.ID = idarray.get(0);
	}
	
	//@Test
	@Order(3)
	public void testRead() {
		//스트림
		boardRepo.findById(BoardRepositoryTests.ID).ifPresent((board) -> {
			System.out.println(board);
		});
	}
	
	//@Test
	@Order(4)
	public void testUpdate() {
		
		//스트림
		boardRepo.findById(BoardRepositoryTests.ID).ifPresent((board) -> {
			System.out.println(board);
		});
		
		System.out.println("first read : ");
		boardRepo.findById(BoardRepositoryTests.ID).ifPresent((board) -> {
			System.out.println("update title : ");
			board.setTitle("제목이 수정되었습니다. 2");
			System.out.println("save table : ");
			boardRepo.save(board);
		});
	
		//스트림
		boardRepo.findById(BoardRepositoryTests.ID).ifPresent((board) -> {
			System.out.println(board);
		});
		
	}
	
	//@Test
	@Order(5)
	public void testDelete() {
		
		//모든 것이 pk가 있는 상테에서 실행 됨 이제
		boardRepo.deleteById(BoardRepositoryTests.ID);
	}
	
	
	//@Test
	@Commit
	public void testDeleteAll() {
		
		//모든 것이 pk가 있는 상테에서 실행 됨 이제
		boardRepo.deleteAll();
	}
	
	
	
	
	@Test
	@Order(2)
	public void insertDataList() {
		
		for(int i = 0; i <= 200; i++) {
			Board board = new Board();
			board.setTitle("제옥.."+i);
			board.setContent("내용.."+i+"..채우기");
			board.setWriter("user0"+(i%10));
			boardRepo.save(board);
		}
	}
	
	//@Test
	public void testByTitle() {
		boardRepo.findBoardByTitle("제옥..121").forEach(board -> System.out.println(board));
	}
	
	//@Test
	public void testByTitleAndWriter() {
		boardRepo.findBoardByTitleAndWriter("제옥..121", "user02").forEach(board -> System.out.println("AND : "+board));
		boardRepo.findBoardByTitleOrWriter("제옥..121", "user02").forEach(board -> System.out.println("OR : "+board));
	}
	
	
	//@Test
	public void testByTimestampBetween() {
		
		//이 처리를 해 줘야 함
		String t1 = "2022-10-19 21:44:14.853000";
		String t2 = "2022-10-19 21:44:14.900000";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
		Timestamp data1 = Timestamp.valueOf(LocalDateTime.parse(t1, formatter));
		Timestamp data2 = Timestamp.valueOf(LocalDateTime.parse(t2, formatter));
		
		boardRepo.findBoardByRegDateBetween(data1, data2).forEach(board -> System.out.println(board));
		
	}
	
	private int count;
	
	//@Test
	public void testByBnoRange() {
		
		//이 처리를 해 줘야 함
		Long t1 = Long.valueOf(110);
		
		
		//로컬 count는 카운팅이 되지 않는다. 람다 식안의 로직은 내부클래스의 형태다. 클래스 안에 전역 변수로서 설정해야 적용이 가능하다.
		System.out.println("미만 값");
		count = 0;
		boardRepo.findBoardByBnoLessThan(t1).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		System.out.println("이하 값");
		count = 0;
		boardRepo.findBoardByBnoLessThanEqual(t1).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");

		System.out.println("초과 값");
		count = 0;
		boardRepo.findBoardByBnoGreaterThan(t1).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		System.out.println("이상 값");
		count = 0;
		boardRepo.findBoardByBnoGreaterThanEqual(t1).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
	}
	
	//@Test
	public void testByRegDataRange() {
		
		//이 처리를 해 줘야 함
		//이 처리를 해 줘야 함
		String t1 = "2022-10-19 21:44:14.853000";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
		Timestamp data1 = Timestamp.valueOf(LocalDateTime.parse(t1, formatter));
		
		
		//After와 Before는 기준이 되는 값은 포함시키지 않는다.
		System.out.println("이후의 값");
		count = 0;
		boardRepo.findBoardByRegDateAfter(data1).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		

		System.out.println("이전의 값");
		count = 0;
		boardRepo.findBoardByRegDateBefore(data1).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
	}	
	
	//@Test
	public void testByContentNullCheck() {
		
		//DB 안에서 Null 인 값이다. ("") 공백 값은 체크 안함  
		System.out.println("이후의 값");
		count = 0;
		boardRepo.findBoardByContentIsNull().forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		

		System.out.println("이전의 값");
		count = 0;
		boardRepo.findBoardByContentIsNotNull().forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
	}
	
	
	//@Test
	public void testByContentLike() {
		
		//단순 Like의 값이다. % % 처리를 하지 않는다.
		count = 0;
		boardRepo.findBoardByContentLike("게시물").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		count = 0;
		boardRepo.findBoardByContentStartingWith("게시물").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		count = 0;
		boardRepo.findBoardByContentEndingWith("채우기").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
	
		count = 0;
		boardRepo.findBoardByContentContaining("00").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
	}
	
	//paging 기능 포함
	//@Test
	public void testByContentLikeOrderBy() {
		
		// 3개 씩 조회 , 오늘차순 정렬, bno
		Pageable paging = PageRequest.of(0, 3, Sort.Direction.ASC, "bno");
		
		
		Page<Board> pageResult1 = boardRepo.findBoardByContentContainingOrderByBnoDesc("게시물",paging);
		System.out.println("PAGE SIZE : "+pageResult1.getSize());
		System.out.println("TOTAL SIZE : "+pageResult1.getTotalPages());
		System.out.println("TOTAL COUNT : "+pageResult1.getTotalElements());
		System.out.println("NEXT : "+pageResult1.nextPageable());
		
		List<Board> list = pageResult1.getContent();
		
		count = 0;
		list.forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		count = 0;
		boardRepo.findBoardByContentContainingOrderByBnoAsc("게시물",paging).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
	}
	
	//@Test
	public void testByBnoIn() {
		
		//Collection > List > ArrayList, Vector, LinkedList, Stack
		//Collection > Set > HashSet, SortedSet
		
		List<Long> inValue = new ArrayList<>();
		
		inValue.add(Long.valueOf(111));
		inValue.add(Long.valueOf(112));
		inValue.add(Long.valueOf(113));
		inValue.add(Long.valueOf(114));
		
		count = 0;
		boardRepo.findBoardByBnoIn(inValue).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		count = 0;
		boardRepo.findBoardByBnoNotIn(inValue).forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
	}
	
	//@Test
	public void testQuery() {
		
		//Collection > List > ArrayList, Vector, LinkedList, Stack
		//Collection > Set > HashSet, SortedSet
		
		count = 0;
		boardRepo.findByTitleQuery("게시물").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		count = 0;
		boardRepo.findByContentQuery("0").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		//[주의]모든 것을 조회하는 것이 아닐때 받아오는 데이터는 Collection<board> 타입이 아닌 Collection<Object[]> 배열의 형태를 취한다.
		count = 0;
		boardRepo.findByContentQueryOnlyBno("0").forEach(arr -> {System.out.println(Arrays.toString(arr)); count++;});
		System.out.println("----------"+count+"------------");
		
		count = 0;
		boardRepo.findByContentNativeQueryOnlyBno("0").forEach(arr -> {System.out.println(Arrays.toString(arr)); count++;});
		System.out.println("----------"+count+"------------");
		
		// 3개 씩 조회 , 오늘차순 정렬, bno
		Pageable paging = PageRequest.of(0, 10, Sort.Direction.ASC, "bno");
		
		
		Page<Board> pageResult1 = boardRepo.findByPageQuery(paging);
		System.out.println("PAGE SIZE : "+pageResult1.getSize());
		System.out.println("TOTAL SIZE : "+pageResult1.getTotalPages());
		System.out.println("TOTAL COUNT : "+pageResult1.getTotalElements());
		System.out.println("NEXT : "+pageResult1.nextPageable());
		
		List<Board> list = pageResult1.getContent();
		
		count = 0;
		list.forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
	}
	
	
	//@Test
	public void testQueryDsl() {
		
	}
	
}
