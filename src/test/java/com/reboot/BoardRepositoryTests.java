package com.reboot;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.reboot.domain.Board;
import com.reboot.repository.BoardRepository;

@SpringBootTest // 2.5.x 버전 이후 @RunWith은 사라지고 @SpringBootTest에 포함됨
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardRepositoryTests {
	
	@Autowired
	private BoardRepository boardRepo;
	
	public static Long ID;	
	
	@Test
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
	
	@Test
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
	
	@Test
	@Order(3)
	public void testRead() {
		//스트림
		boardRepo.findById(BoardRepositoryTests.ID).ifPresent((board) -> {
			System.out.println(board);
		});
	}
	
	@Test
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
	
	@Test
	@Order(5)
	public void testDelete() {
		
		//모든 것이 pk가 있는 상테에서 실행 됨 이제
		boardRepo.deleteById(BoardRepositoryTests.ID);
	}
	
	@Test
	@Order(6)
	public void insertDataList() {
		
		for(int i = 0; i <= 200; i++) {
			Board board = new Board();
			board.setTitle("제옥.."+i);
			board.setContent("내용.."+i+"..채우기");
			board.setWriter("user0"+(i%10));
			boardRepo.save(board);
		}
	}
	
}
