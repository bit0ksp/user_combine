package com.spring.boardFree;

import lombok.Data;

@Data
public class ThumbVO { // 추천/비추천 공통 VO
	private int bf_thumb;
	private int bfr_rno;
	private int bf_bno;
	private int id;
}
