package com.reboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reboot.domain.DataVO;
import com.reboot.domain.SampleVO;

@RestController
public class RebootController {

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello world";
	}
	
	@GetMapping("/makeSample")
	public SampleVO makeSampleVo() {
		
		SampleVO vo = new SampleVO();
		
		vo.setVal1("String1");
		vo.setVal2("String2");
		vo.setVal3("String3");
		vo.setVal4("String4");
		
		return vo;
	}
	
	@GetMapping("/makeData")
	public DataVO makeDataVo() {
		
		DataVO vo = new DataVO();
		
		vo.setData01("001");
		vo.setData02("002");
		vo.setData03("003");
		vo.setData04("004");
		
		return vo;
	}
	
}
