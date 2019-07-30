package com.spring.board;
/**
* @Class Name : ShareVO.java

* @Description : 나눔게시판 VO
* @Modification Information
* @
* @  수정일     	  수정자                 수정내용
* @ ---------   ---------   -------------------------------
* @ 2019.07.22     박현민      최초생성
* @author bit 2조
* @since 2019. 07.01
* @version 1.0
* @see
*
*  Copyright (C) by Bit All right reserved.
*/

import java.util.Date;

import lombok.Data;

@Data
public class ShareVO {
	private int bs_bno;//	BS_BNO
	private int id;//	ID
	private String bs_category;//	BS_CATEGORY
	private String bs_content;//	BS_CONTENT
	private String bs_title;//	BS_TITLE
	private Date bs_reg_date;//	BS_REG_DATE
	private Date bs_update_date;//	BS_UPDATE_DATE
	private int bs_view_counter;//	BS_VIEW_COUNTER
	private int bs_warn_counter;//	BS_WARN_COUNTER
	private String bs_deleteyn;//	BS_DELETEYN
}