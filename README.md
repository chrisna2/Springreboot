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

	```


6. Spring 필터링 기본 구조 : Filter > Interceptopr > Aop > Interceptor > Filter

- 내가 이번 면접에서 실패 했던 부분, 마음이 아프고 제대로 공부 하지 못했다는 반성을 하기 위해 다시 공부한다.
- 자바에서 공통적으로 처리해야 할 업무 들은 위에 필터와 인터셉터 AOP를 통해 처리 된다.
- 공통 업무의 주요 부분은 로그인 관련 세션 체크, 권한체크, Xss방어, PC웹과 모바일웹 분기 처리 , 로깅, 페이지 인코딩 등을 수행한다.
- 각각의 서비스 코드에서 그 모든 부분을 처리 하게 된다면 중복코드가 많아지고 소스관리가 되지 않아 공통부분의 기능을 분리하게 위해 스프링에는 해당 기능을 통해 기능들을 관리하게 된다.

![aop_inter_filter](./img/aop_inter_filter.png)

- Interceptor와 Filter는 Servlet 단위에서 실행된다. 
- 반면 AOP는 메소드 앞에 Proxy패턴의 형태로 실행된다.
- 실행 순서를 보면 **Filter가 가장 밖에 있고 그 안에 Interceptor, 그 안에 AOP가 있는 형태이다.** 이 부분을 무식하게 AOP > Interceptor > FIlter 순이라고 섫명했다.. 무친...
- Request -> Filter -> InterCeptopr -> AOP -> Controller(서비스) -> AOP -> InterCeptor -> Filter -> Response 이 순서로 이루어진다.  

- 메소드 실행관점에서 본 순서
1) 서버을 실행 시키며 서블릿이 올라오는 동안 init이 실행 되면 그 후 dofilter가 실행된다. 
2) 컨드롤러 들어가기 전 preHandler 가 실행된다.
3) 컨트롤러에서 나와 PostHandler, after completion, dofilter 순으로 실행된다. 
4) 서블릿 종료시 destroy가 실행된다.


- Filter
1) 요청과 응답을 거른뒤 정제하는 역활
2) 서블릿 필터는 DispatchServlet 이전에 실행이 되는 데 필터가 동작하도록 지정된 자원의 앞단에서 요청 내용을 변경하거나, 여러가지 체크를 수행할 수 있다.
3) 주 사용처 : 인코딩 변환 처리, XSS 방어, 입력데이터 검증 등 
4) 필터의 실행 메서드 : init() 필터의 인스턴스 초기화, doFilter() 전/후 처리, destroy() 필터 종료




















