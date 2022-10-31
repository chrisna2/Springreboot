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
	- 회원과 프로필 사진들의 관계 : 일대다 - 다대일 - 단방향 : 회원(1) <- 프로필사진(Many) 
	- 자료실 첨부 파일의 관계 : 일대다 - 다대일 - 단방향 : 자료실자료(1) -> 첨부파일(Many) 
	- 게시물과 댓글의 관계 : 일대다 - 다대일 - 단방향 : 게시물(1) <-> 댓글(Many) 
	
### [1] 회원과 프로필 사진들의 관계 : 일대다 - 다대일 - 단방향 : 회원(1) <- 프로필사진(Many) 



### [2] 자료실 첨부 파일의 관계 : 일대다 - 다대일 - 단방향 : 자료실자료(1) -> 첨부파일(Many) 
### [3] 게시물과 댓글의 관계 : 일대다 - 다대일 - 단방향 : 게시물(1) <-> 댓글(Many) 

## 2. 단방향, 양방향 관계의 이해




## 3. JPQL을 이용한 @Query 처리와 Fetch JOIN