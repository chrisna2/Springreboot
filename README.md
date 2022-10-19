# 스프링 부트 기억되살리기
### 2022-10-19

1. 개발환경 설정
2. 프로젝트 생성
3. 롭복라이브러리
4. 로컬 테스트 서버 실행 및 포트 변경
5. JPA 테스트 , hibernate - progresql, mariadb
	```
	***************************
	APPLICATION FAILED TO START
	***************************
	
	Description:
	
	Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
	
	Reason: Failed to determine a suitable driver class
	
	
	Action:
	
	Consider the following:
		If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
		If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are 	currently active).
	```
	
- 해당 에러가 나는 이유는 스프링 부트 내에 JDBC 등의 설정이 포함되어 있는데 이에 대한 설정이 없기 때문
- application.properties 를 통해 해당 설정을 추가해야 됨

```properties
# JPA 데이터 소스 연결 : 마리아 DB
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://127.0.0.1:3306/jpa_ex
spring.datasource.username=jpa_user
spring.datasource.password=1234qwer!
```
- 쿼리 메소드 
```java
public interface BoardRepository extends CrudRepository<Board, Long>{
	
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
	public List<Board> findBoardByContentContainingOrderByBnoDesc(String content);
	public List<Board> findBoardByContentContainingOrderByBnoAsc(String content);
	
	//In : In
	public List<Board> findBoardByBnoIn(Collection<Long> bno);
	//Not In : NotIn
	public List<Board> findBoardByBnoNotIn(Collection<Long> bno);
}
```
