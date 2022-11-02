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
		- 영속성 전의 : **처리하려는 엔티티 객체의 상태에 따라서 종속적인 객체들의 영속성도 같이 처리되는 것**
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




### [4] JPA 연관 관계 설정 annotation 정리

* RDB는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없기 때문에 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어낸다.
* 하지만 객체에서는 Collection을 사용해서 객체 2개로 다대다 관계를 나태낼 수 있다.
* @ManyToMany 사용 
* @JoinTable로 연결 테이블 지정 
* 다대다 매핑: 단방향, 양방향 가능

```java
//Product

@ManyToMany(mappedBy = "products")
private List<Member> members = new ArrayList<>();
```
```java
//Member

@ManyToMany
@JoinTable(name = "MEMBER_PRODUCT") //MEMBER_PRODUCT라는 연결 테이블이 생성됨
private List<Product> products = new ArrayList<>();
```

#### 다대다 매핑의 한계
* 편리해 보이지만 실무에서는 사용해서는 안 된다.
* 연결 테이블이 단순히 연결만 하고 끝나지 않는다.
* 매핑 정보만 들어가고 주문시간, 수량같은 필드를 추가할 수 없다.
* 중간 테이블이 숨겨져 있기 때문에 쿼리가 이상하게 만들어진다.

#### 다대다 한계 극복
* 연결 테이블용 Entity를 추가한다.
* @ManyToMany → @OneToMany, @ManyToOne

```java
//MemberProduct

@Id @GeneratedValue
private Long id;

@ManyToOne
@JoinColumn(name = "MEMBER_ID")
private Member member;

@ManyToOne
@JoinColumn(name = "PROUDCT_ID")
private Product product;
```
```java
//Member

@OneToMany(mappedBy = "member")
private List<MemberProduct> memberProducts = new ArrayList<>();
```
```java
//Product

@OneToMany(mappedBy = "product")
private List<MemberProduct> memberProducts = new ArrayList<>();
```
* 연결 테이블의 PK는 종속 테이블들의 PK들의 복합키로 사용하지 말고 따로 PK를 만들고 FK들은 비식별 관계로 나타낸다.
* 키가 어딘가에 종속되는 경우 유연성 있게 바꾸기 어렵기 때문
* 그렇기 때문에 PK 하나만 선언해주는 게 유연성 있게 사용할 수 있다.

#### @JoinColumn
* 외래 키를 매핑할 때 사용한다.

<table>
    <tr>
        <th>속성</th>
        <th>설명</th>
        <th>기본값</th>
    </tr>
    <tr>
        <td>name</td>
        <td>매핑할 외래 키 이름</td>
        <td>필드명 + _ + 참조하는 테이블의 기본 키 컬럼명</td>
    </tr>
    <tr>
        <td>referencedColumnName</td>
        <td>외래 키가 참조하는 대상 테이블의 컬럼명</td>
        <td>참조하는 테이블의 기본 키 컬럼명</td>
    </tr>
    <tr>
        <td>foreignKey(DDL)</td>
        <td>
            외래 키 제약조건을 직접 지정할 수 있다. <br>
            이 속성은 테이블을 생성할 때만 사용한다.
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            unique <br>
            nullable insertable <br>
            updatable <br>
            columnDeﬁnition <br>
            table <br>
        </td>
        <td>@Column의 속성과 같다.</td>
        <td></td>
    </tr>
</table>

#### @ManyToOne
* 다대일 관계 매핑

<table>
    <tr>
        <th>속성</th>
        <th>설명</th>
        <th>기본값</th>
    </tr>
    <tr>
        <td>optional</td>
        <td>false로 설정하면 연관된 엔티티가 항상 있어야 한다.</td>
        <td>true</td>
    </tr>
    <tr>
        <td>fetch</td>
        <td>글로벌 페치 전략을 설정한다.</td>
        <td>
            @ManyToOne=FetchType.EAGER <br>
            @OneToMany=FetchType.LAZY
        </td>
    </tr>
    <tr>
        <td>cascade</td>
        <td>속성 전이 기능을 사용한다.</td>
        <td></td>
    </tr>
    <tr>
        <td>targetEntity</td>
        <td>연관된 엔티티의 타입 정보를 설정한다. 이 기능은 거의 사용하지 않는다. 컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있다.</td>
        <td></td>
    </tr>
</table>

#### @OneToMany
* 다대일 관계 매핑

<table>
    <tr>
        <th>속성</th>
        <th>설명</th>
        <th>기본값</th>
    </tr>
    <tr>
        <td>mappedBy</td>
        <td>연관관계의 주인 필드를 선택한다.</td>
        <td></td>
    </tr>
    <tr>
        <td>fetch</td>
        <td>글로벌 페치 전략을 설정한다.</td>
        <td>
            @ManyToOne=FetchType.EAGER <br>
            @OneToMany=FetchType.LAZY
        </td>
    </tr>
    <tr>
        <td>cascade</td>
        <td>속성 전이 기능을 사용한다.</td>
        <td></td>
    </tr>
    <tr>
        <td>targetEntity</td>
        <td>연관된 엔티티의 타입 정보를 설정한다. 이 기능은 거의 사용하지 않는다. 컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있다.</td>
        <td></td>
    </tr>
</table>

#### FetchType의 LAZY와 EAGER
* LAZY
    * 지연로딩
    * 연관관계가 설정된 테이블에 대해 select를 하지 않는다.
    * 1:N 과 같이 여러가지 데이터가 로딩이 일어날 경우 사용하는 방식
* EAGER
    * 즉시로딩
    * 연관관계가 설정된 모든 테이블에 대해 조인이 이루어진다.
    * 1:1 연관관계와 같이 한 건만 존재할 때 사용하는 방식



