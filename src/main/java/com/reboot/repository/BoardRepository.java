package com.reboot.repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reboot.domain.Board;
//spring 2.7.4 버전 queryDsl 기본적으로 5.0.0 버전이 깔려 있으나 인식이 안되어 4.1.4로 설정하여 처리
public interface BoardRepository extends JpaRepository<Board, Long>{
	
	//인터페이스 상속 구조 
	//Repository (기능없음) > CrudRepository (CRUD) > PagingAndSortingRepository (페이징 기능 추가) > JpaRepository (JPA에 특화된 기능 추가)
	
	//쿼리 메소드
	/*
		find...By...	
		read...By...	
		query...By...	
		get...By...		
		count...By...	
	*/
	//Repository의 구성 데이터 List<Board>, Slice<Board>, Page<Board>
	public List<Board> findBoardByTitle(String title);
	
	// AND 조건 : And 추가 A And B
	public List<Board> findBoardByTitleAndWriter(String title, String writer);
	
	// OR 조건 : Or 추가 A Or B
	public List<Board> findBoardByTitleOrWriter(String title, String writer);
	
	// Between 조건 : Between 추가
	public List<Board> findBoardByRegDateBetween(Timestamp t1, Timestamp t2);
	
	// < 미만 값 찾기  : LessThan
	public List<Board> findBoardByBnoLessThan(Long bno);
	
	// <= 이하 값 찾기  : LessThanEqual
	public List<Board> findBoardByBnoLessThanEqual(Long bno);
	
	// > 초과 값 찾기  : GreaterThan
	public List<Board> findBoardByBnoGreaterThan(Long bno);
	
	// >= 이상 값 찾기  : GreaterThanEqual
	public List<Board> findBoardByBnoGreaterThanEqual(Long bno);
	
	//After와 Before는 기준이 되는 값은 포함시키지 않는다.
	
	// After 일자 이후 : After
	public List<Board> findBoardByRegDateAfter(Timestamp t1);
	
	// Null 조회 : IsNull
	public List<Board> findBoardByRegDateBefore(Timestamp t1);
	
	//DB 안에서 Null 인 값이다. ("") 공백 값은 체크 안함  
	
	// Null 조회 : IsNull
	public List<Board> findBoardByContentIsNull();
	
	// Not Null 조회 : IsNotNull, NotNull
	public List<Board> findBoardByContentIsNotNull();
	public List<Board> findBoardByContentNotNull();
	
	//단순 Like의 값이다. % % 처리를 하지 않는다.
	
	//Like 문구 : Like
	public List<Board> findBoardByContentLike(String content);
	//Not Like 문구 : NotLike
	public List<Board> findBoardByContentNotLike(String content);
	
	
	//어두 찾기 : StartingWith 
	public List<Board> findBoardByContentStartingWith(String content);
	
	//어미 찾기 : EndingWith 
	public List<Board> findBoardByContentEndingWith(String content);
	
	//% % 찾기 : Containing
	public List<Board> findBoardByContentContaining(String content);
	
	//order by : 찾기 조건 모두 완료 후 맨 끝에 OrderBy A Desc(Asc)
	//Hibernate: select board0_.bno as bno1_0_, board0_.content as content2_0_, board0_.reg_date as reg_date3_0_, board0_.title as title4_0_, board0_.update_date as update_d5_0_, board0_.writer as writer6_0_ from tbl_board board0_ where board0_.content like ? escape ? order by board0_.bno desc, board0_.bno asc limit ?
	//Hibernate: select count(board0_.bno) as col_0_0_ from tbl_board board0_ where board0_.content like ? escape ?
	public Page<Board> findBoardByContentContainingOrderByBnoDesc(String content, Pageable page);
	public Page<Board> findBoardByContentContainingOrderByBnoAsc(String content, Pageable page);
	
	//In : In
	public List<Board> findBoardByBnoIn(Collection<Long> bno);
	//Not In : NotIn
	public List<Board> findBoardByBnoNotIn(Collection<Long> bno);

	//---------이상 잘 안쓰이는 쿼리 메소드 --------------------
	
	
	//---------실질적으로는 @query가 많이 쓰인다. -> 서버 로딩시 생성된 쿼리를 해석하니 해당 쿼리메 대한 검증은 필수
	
	// 파라미터의 갯수 순서에 따라 ?1 ?2 ?3 ... 이런식으로 늘어난다.
	//Hibernate: select board0_.bno as bno1_0_, board0_.content as content2_0_, board0_.reg_date as reg_date3_0_, board0_.title as title4_0_, board0_.update_date as update_d5_0_, board0_.writer as writer6_0_ from tbl_board board0_ where (board0_.title like ?) and board0_.bno>0 order by board0_.bno DESC
	@Query("SELECT b FROM Board b WHERE b.title LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
	public List<Board> findByTitleQuery(String Title);
	
	
	// 파라미터의 이름을 직접 명시도 가능
	//Hibernate: select board0_.bno as bno1_0_, board0_.content as content2_0_, board0_.reg_date as reg_date3_0_, board0_.title as title4_0_, board0_.update_date as update_d5_0_, board0_.writer as writer6_0_ from tbl_board board0_ where (board0_.content like ?) and board0_.bno>0 order by board0_.bno DESC
	@Query("SELECT b FROM Board b "
			+"WHERE b.content LIKE %:content% "
			+"AND b.bno > 0 ORDER BY b.bno DESC")//이런 식으로 줄바꿈 처리도 가능
	public List<Board> findByContentQuery(@Param("content")String Title);
	
	//[주의]모든 것을 조회하는 것이 아닐때 받아오는 데이터는 Collection<board> 타입이 아닌 Collection<Object[]> 배열의 형태를 취한다.
	@Query("SELECT b.bno, b.title FROM Board b "
			+"WHERE b.content LIKE %:content% "
			+"AND b.bno > 0 ORDER BY b.bno DESC")//이런 식으로 줄바꿈 처리도 가능
	public List<Object[]> findByContentQueryOnlyBno(@Param("content")String Title);
	
	//Native query 사용 -> DBMS에 사용하고 있는 쿼리를 그대로 적용하여 사용 , 기존 까지는 hibernate 문법이고 이건 mybatis와 같은 형태
	//기존에 사용하고 있는 query를 그대로 사용한다는 장점이 있지만 DBMS에 독립적이라는 장점은 포기해야함 (DBMS 마다 query문법이 다르기 때문)
	@Query(value = "SELECT bno, title FROM tbl_board "
			+"WHERE content LIKE %:content% "
			+"AND bno > 0 ORDER BY bno DESC", nativeQuery = true)//이런 식으로 줄바꿈 처리도 가능
	public List<Object[]> findByContentNativeQueryOnlyBno(@Param("content")String Title);
	
	
	// 쿼리 메소드와 마찬가지로 페이징 처리도 가능함
	//Hibernate: select board0_.bno as bno1_0_, board0_.content as content2_0_, board0_.reg_date as reg_date3_0_, board0_.title as title4_0_, board0_.update_date as update_d5_0_, board0_.writer as writer6_0_ from tbl_board board0_ where board0_.bno>0 order by board0_.bno DESC, board0_.bno asc limit ?
	//Hibernate: select count(board0_.bno) as col_0_0_ from tbl_board board0_ where board0_.bno>0
	@Query("SELECT b FROM Board b "
			+"WHERE b.bno > 0 ORDER BY b.bno DESC")//이런 식으로 줄바꿈 처리도 가능
	public Page<Board> findByPageQuery(Pageable page);
	
	
	//---------이상 잘쓰이는 @query --------------------
	
	
	// mybatis와 마찬가죄 동적 쿼리 생성이 가능하다. queryDsl 사용하면 됨
	
	
	
	
	

	
	
}
