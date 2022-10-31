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
	- 게시물과 댓글의 관계 : 일대다 - 다대일 - 단방향 : 게시물(1) <-> 댓글(Many) 
	
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
				
 - 비싼 강의를 사놓고 결국 완강하지 못하는 이유, 결과를 내지 못하는 이유는 꾸준하지 못하기 때문이다. 조금이라도 매일 쌓아 올려놨어야 했다.	  			

|주문 날짜|주문명|금액|	
|----------|------------------------------------------------|---------|
|2022-09-26|실제 이력서 사례로 알아보는 [합격하는 이력서] 작성 가이드		|₩49,500|	
|2022-09-26|자바(Java) 알고리즘 문제풀이 : 코딩테스트 대비				|₩77,000|	
|2020-09-03|실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화 외 1건	|₩132,000|	
|2019-11-15|스프링 프레임워크 핵심 기술 외 1건						|₩132,000|	
|2019-11-15|스프링 부트 개념과 활용 외 1건							|₩159,500|	
|2019-11-15|스프링 프레임워크 개발자를 위한 실습을 통한 입문 과정			|₩11,000|	
|2019-11-15|누구든지 하는 리액트: 초심자를 위한 react 핵심 강좌			|₩16,500|	
|2019-11-15|입문에서 실무까지: DevOps의 이해 및 Docker Hands-on		|₩33,000|	
|2022-10-31|Kubernetes와 Docker로 한 번에 끝내는 컨테이너 기반 MSA	|₩322,000|
|2018-12-28|슈퍼패스 : 개발자 취업(컴공/프첫/데분입)					|₩239,000|

총합이 **1,171,500** 원이다. 		
				
### [3] 게시물과 댓글의 관계 : 일대다 - 다대일 - 단방향 : 게시물(1) <-> 댓글(Many) 

## 2. 단방향, 양방향 관계의 이해




## 3. JPQL을 이용한 @Query 처리와 Fetch JOIN
