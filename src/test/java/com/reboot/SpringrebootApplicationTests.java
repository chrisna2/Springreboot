package com.reboot;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.boot.test.context.SpringBootTest;

import com.reboot.repository.BoardRepository;

@SpringBootTest
class SpringrebootApplicationTests {
	
	@Autowired
	private BoardRepository boardRepo;
	
	@Test
	void contextLoads() {
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
	
	//@Test
	public void testByContentLikeOrderBy() {
		
		count = 0;
		boardRepo.findBoardByContentContainingOrderByBnoDesc("게시물").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
		count = 0;
		boardRepo.findBoardByContentContainingOrderByBnoAsc("게시물").forEach(board -> {System.out.println(board); count++;});
		System.out.println("----------"+count+"------------");
		
	}
	
	@Test
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
		
}
