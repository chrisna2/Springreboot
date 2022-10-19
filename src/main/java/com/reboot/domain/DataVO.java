package com.reboot.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"data01"})
public class DataVO {

	private String data01;
	private String data02;
	private String data03;
	private String data04;
	
	
}
