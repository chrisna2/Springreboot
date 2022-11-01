# JPA 연관관계

## 1. 객체 간 연관관계 설정  

- 연관관계 처리순서와 서전 설계    
> 1) 필요한 각각의 클래스를 정의합니다    
> 2) 각 클래스의 연관관계에 대한 설정을 추가합니다.  
>	A. "일대다", "다대다"등의 연관 관계 설정  
>	B. 단방향, 양방향 설정  
> 3) 데이터베이스상에 원하는 형태의 테이블이 만들어지는지 확인   
> 4) 테스트 코드를 통해 정상적으로 동적하는지 확인  
  
- 관계형 데이터베이스의 설계  
> 1) 가장 중심이 되는 사람이나 명사를 결정하고, 이에 대한 구조를 대략적으로 설계한다.  
> 2) 1)에서 생성된 데이터들이 상호작용을 하면서 만들어내는 새로운 데이터를 정의합니다.  
> 3) 1)과 2)를 세분화화 해서 설계한다.   
  
- 전통적 RDBMS 연관관계
> 1) 일대일  
> 2) 일대다  
> 3) 다대다  

- JPA 에서의 위에 관계의 표현 
> 1) @OneToOne : 일대일
> 2) @OneToMany : 일대다
> 3) @ManyToOne : 다대일
> 4) @ManyToMany : 다대다

- JPA 방향 참조 
> 1) 단방향 참조 : 한쪽의 클래스만이 다른 클래스의 인스턴스를 참조하도록 설정  
> 2) 양방향 참조 : 양쪽 클래스 모두 다른 클래스의 인스턴스를 참조하도록 설정

- 작성 예제의 개요 
	- 회원과 프로필 사진들의 관계 : 일대다 - 다대일 - 단방향 : 회원(1) <- **프로필사진(Many)**
	- 자료실 첨부 파일의 관계 : 일대다 - 다대일 - 단방향 : 자료실자료(1) -> **첨부파일(Many)** 		
	- 게시물과 댓글의 관계 : 일대다 - 다대일 - 단방향 : **게시물(1)** <-> **댓글(Many)** 
	
### [1] 회원과 프로필 사진들의 관계 : 일대다 - 다대일 - 단방향 : 회원(1) <- 프로필사진(Many) 

	1. 방향을 설정하려는 엔티티에 방향을 설정한다 : **프로필사진(Profile.java)**
	```java
		@Getter
		@Setter
		@ToString(exclude = "member")//멤버는 제오 시킴
		@Entity
		@Table(name="tbl_profile")
		@EqualsAndHashCode(of="fno")
		public class Profile {
			
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Long fno;
			
			private String fname;
			
			private boolean current;
			
			/* 단방향 설정 프로핑에서 회원으로 접근만 가능 */
			@ManyToOne // 프로파일 Many : 멤버 One | 프로필 -> 멤버 
			private Member member;
		
		}
	```

### [2] 자료실 첨부 파일의 관계 : 일대다 - 다대일 - 단방향 : 자료실자료(1) -> 첨부파일(Many) 
	1. 방향을 설정하려는 엔티티에 방향을 설정한다 : **자료실자료(PDSBoard.java)**  
	```java
		@Getter
		@Setter
		@ToString(exclude = "files")
		@Entity
		@Table(name="tbl_pds")
		@EqualsAndHashCode(of="pid")
		public class PDSBoard {
			
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Long pid;
			
			private String pname;
			
			private String pwriter;
			
			@OneToMany 
			// 자료 -> 첩부파일 | 자료 : 1 - 첨부파일 : Many 
			@JoinColumn(name="pdsno") 
			//pdsno라는 신규 컬럼이 PDSFile에 생성되고 이는 pdsfile의 fno가 들어 가게 된다. 
			//이걸 안하면 의도와 다르게 연관 테이블이 또 생성되게 됨
			private List<PDSFile> files;
		
		}			
	```
	
	2. 영속성 전의
		- 영속성 전의 : 처리하려는 엔티티 객체의 상태에 따라서 종속적인 객체들의 영속성도 같이 처리되는 것
			예시) 날짜와 일정의 관계 : 특정한 날짜가 데이터 베이스에서 사라지면 해당 날짜에 속하는 일정도 같이 사라져야 한다.
		- jpa에서 엔티티들의 기본적으로 메모리 상의 관계 날짜객체가 사라지면 일정객체도 같이 삭제될 필요가 있음
		- 부모 앤티티나 자식 엔티티의 상태 변화가 자신과 관련있는 엔티티에 영향을 주는 것을 의미
	
		즉 테이블의 조인으로 묶여 있는경우 해당 조인에 종속된 자식 테이블의 데이터도 같이 처리되어야 한다.
		
		- 영속성 전의 Options 
			1) **ALL : 부모테이블의 모든 변경(CRUD)에 대하여 자식테이블의 전의**  
			2) PRESIST : 저장 시에만 전의 
			3) MERGE : 병합 시에만 전의
			4) REMOVE : 삭제 시에만 전의
			5) REFRESH : 엔티티 매니저의 refresh() 호출 시 전의
			6) DETACH : 부모 엔티티가 detach되면 자식 엔티티 역시 detach  
			
		- 사용방법 
		```java
			@OneToMany(cascade = CascadeType.ALL)
			@OneToMany(cascade = CascadeType.PERSIST)
			@OneToMany(cascade = CascadeType.MERGE)
			@OneToMany(cascade = CascadeType.REMOVE)
			@OneToMany(cascade = CascadeType.REFRESH)
			@OneToMany(cascade = CascadeType.DETACH)
		```
	
	3. hibernate SQL 과 실제 DBMS 쿼리의 차이
		
		1> hibernate JPA로 적용되는 쿼리
				
		```java
				@Query("SELECT p, count(f) "
				+ "FROM PDSBoard p "
					+ "LEFT OUTER JOIN p.files f "
				+ "WHERE p.pid > 0 "
				+ "GROUP BY p "
				+ "ORDER BY pid DESC ")
		
		```
		 
		2> hibernate 로 변경되어 적용된 db 쿼리
		 
		```sql		 
		 select 
		 	pdsboard0_.pid as col_0_0_, 
		 	count(files1_.fno) as col_1_0_, 
		 	pdsboard0_.pid as pid1_2_, 
		 	pdsboard0_.pname as pname2_2_, 
		 	pdsboard0_.pwriter as pwriter3_2_ 
		 from tbl_pds pdsboard0_ 
		 	left outer join tbl_pds_files files1_ 
		 	on pdsboard0_.pid=files1_.pdsno 
		 where pdsboard0_.pid>0 
		 group by pdsboard0_.pid 
		 order by pid DESC
		```
				 
		3> 실제 DBMS 쿼리
				 
		```sql
		SELECT p.*, COUNT(f.fno) 
		FROM TBL_PDS p 
			LEFT OUTER JOIN tbl_pds_files f 
			ON f.pdsno = p.pid
		WHERE p.pid > 0 
		GROUP BY p.pid
		ORDER BY p.pid DESC ;
		```
			
		hibernate 쿼리와 hibernate 쿼리로 변경한 실제 쿼리는
		문법이 다르다. 이걸 확인해야 된다.
		
	
	
				

### [3] 게시물과 댓글의 관계 : 일대다 - 다대일 - 단방향 : 게시물(1) <-> 댓글(Many) 

## 2. 단방향, 양방향 관계의 이해




## 3. JPQL을 이용한 @Query 처리와 Fetch JOIN
