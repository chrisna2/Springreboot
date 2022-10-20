package com.reboot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.reboot.domain.Member;
import com.reboot.domain.Profile;
import com.reboot.repository.MemberRepository;
import com.reboot.repository.ProfileRepository;

import lombok.extern.java.Log;

@SpringBootTest
@Log
@Commit
public class ProfileTest {
	
	@Autowired
	MemberRepository memRepo;
	
	@Autowired
	ProfileRepository proRepo;
	
	
	//@Test
	public void testInsertMember() {
		
		IntStream.range(1, 101).forEach(i -> {
			
			Member mem = new Member();
			
			mem.setUid("user"+i);
			mem.setUpw("pw"+i);
			mem.setUname("사용자"+i);
			
			memRepo.save(mem);
			
		});
	}
	//@Test
	public void testInsertProfile() {
		
		Member member = new Member();
		member.setUid("user1");
		
		for(int i = 1;i <= 5; i++) {
			Profile profile1 = new Profile();
			profile1.setFname("face"+i+".jpg");
			
			if(i == 1) {
				profile1.setCurrent(true);
			}
			
			profile1.setMember(member);
			
			proRepo.save(profile1);
		}
	}
	
	
	//@Test
	public void testFetchJoin1(){
	
		List<Object[]> result = memRepo.getMemberWithProfileCount("user1");
		
		result.forEach(arr -> {
			System.out.println(Arrays.toString(arr));
		});
		
	}
	
	@Test
	public void testFetchJoin2(){
		
		List<Object[]> result2 = memRepo.getMemberWithCurrentProfile("user1");
		
		result2.forEach(arr -> {
			System.out.println(Arrays.toString(arr));
		});
	}
	
	
}
