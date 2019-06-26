package com.jt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {
	@RequestMapping("/sentMsg")
	public String sentMsg() {
		return"8091";
	}
}
