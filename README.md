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
