package com.reboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.Member;

public interface MemberRepository extends CrudRepository<Member, String> {
	
	
	// left 아우터 조인시 
	@Query("SELECT m.uid, count(p) "
			+ "FROM Member m LEFT OUTER JOIN Profile p "
			+ "ON m.uid = p.member WHERE m.uid = ?1 GROUP BY m")
	public List<Object[]> getMemberWithProfileCount(String uid);
	
	
	@Query("SELECT m, p "
			+ "FROM Member m LEFT OUTER JOIN Profile p "
			+ "ON m.uid = p.member AND p.current = 1"
			+ "WHERE m.uid = ?1 ")
	public List<Object[]> getMemberWithCurrentProfile(String uid);

	

}
