
package com.spring.mypage;
/**
 * @Class Name : MyPageController.java
 * @Description : MyPage Controller
 * @Modification Information
 * @
 * @  수정일          수정자                 수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2019.07.0?     황진석      최초생성
 * @ 2019.07.17     한유진      순서변경, 각 메서드 별 주석추가(기능설명)
 * @ 2019.07.17     한유진      클래스명 변경(LoginController -> MyPageController), 기능구현

 * @ 2019.07.24      박현민      one_get  부분 추가

 * @author bit 2조
 * @since 2019. 07.01
 * @version 1.0
 * @see
 *
 *  Copyright (C) by Bit All right reserved.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.spring.member.MemberDAO;
import com.spring.member.MemberService;
import com.spring.member.MemberVO;
import com.spring.paging.PageMaker;
import com.spring.paging.SearchCriteria;

@Controller
public class MyPageController {

   @Autowired
   private MyPageService myPageService;


   // mypage 메인
   @RequestMapping(value = "/mypage", method = RequestMethod.GET)
   public String mypage(Model model, HttpSession session) {
      int id = myPageService.getMemberId((String) session.getAttribute("m_email"));
      MemberVO member = (MemberVO) myPageService.getMember(id);
      model.addAttribute("member", member);

      // 1:1 문의내역
      List<OneVO> qnaList = null;
      qnaList = myPageService.getQnaList(id);
      model.addAttribute("qnaList", qnaList);
      
      List<folfolDTO> followerList = null;
      followerList = myPageService.getfollower(id);
      model.addAttribute("follower", followerList);
      System.out.println(followerList);
      
      List<folfolDTO> followingList = null;
      followingList = myPageService.getfollowing(id);
      model.addAttribute("following", followingList);
      System.out.println(followingList);

      return "mypage/mypage";
   }

   // 마이페이지 - 비밀번호 확인
   @RequestMapping(value = "/pw_confirm", method = RequestMethod.GET)
   public String pwConfirm(HttpSession session, Model model) {
      int id = myPageService.getMemberId((String) session.getAttribute("m_email"));
      MemberVO member = myPageService.getMember(id);
      model.addAttribute("member", member);

      return "mypage/pw_confirm";
   }



   // 마이페이지 - 비밀번호 수정
   @RequestMapping(value = "/update_pw")
   public String updatePw(Model model, MemberVO memberVO, int id) {

      MemberVO member = myPageService.getMember(id);

      myPageService.updatePw(memberVO);

      member = myPageService.getMember(id);

      model.addAttribute("member", member);
      return "mypage/member_info";
   }


   // 마이페이지 - 닉네임 수정
   @RequestMapping(value = "/update_nick")
   public String updateNick(Model model, MemberVO memberVO, HttpServletResponse response) throws Exception {
	   
	  List<MemberVO> memberList = myPageService.getMembers();
	  System.out.println("memberlist="+memberList);
	  for(int i=0; i<memberList.size(); i++) {
		  MemberVO member = memberList.get(i);
		  
		  String nick = member.getM_nickname();
		  System.out.println("nick="+nick);
		  
		  if(memberVO.getM_nickname().equals(nick)) {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('중복된 닉네임입니다!');");
				out.println("history.go(-1);");
				out.println("</script>");
				out.close();			
				return null;
		  }
	  }
	  
	 
	
	  
	  
      myPageService.updateNick(memberVO);
      
      MemberVO member = myPageService.getMember(memberVO.getId());

      model.addAttribute("member", member);
     
      response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('닉네임이 변경되었습니다.!');");
		out.println("history.go(-1);");
		out.println("</script>");
		out.close();

      return "mypage/member_info";
		
   }

   
   

	
   // 마이페이지 - 회원정보 수정
   @RequestMapping(value = "/member_update")
   public String updateMember(Model model, HttpServletResponse response, MemberVO memberVO, int id) {
	   
      MemberVO member = myPageService.getMember(id);
      myPageService.updateMember(memberVO);

      member = myPageService.getMember(id);


      model.addAttribute("member", member);

      response.setContentType("text/html; charset=UTF-8");
      try {
         PrintWriter out = response.getWriter();
         out.println("<script>");
         out.println("alert('접근 권한이 없습니다.');");
         out.println("</script>");
      } catch (IOException e) {
         e.printStackTrace();
      }

      return "mypage/member_info";
   }



   // 마이페이지 - 비밀번호 재확인 - 회원정보 수정
   @RequestMapping(value = "/member_info", method = RequestMethod.GET)
   public String memberInfo(MemberVO vo, RedirectAttributes rttr, Model model, HttpServletResponse response,HttpSession session) {
      
	   int id = myPageService.getMemberId((String) session.getAttribute("m_email"));
      
      boolean result = myPageService.checkPw(vo.getM_email(), vo.getM_password());
      
      System.out.println(vo.getM_email());
      System.out.println(vo.getM_password());
      
      if (result) {// 비밀번호가 일치하면 사이트 이동
    	  MemberVO member = myPageService.getMember(id);
         // 아이디값을 가져와지긴 하는데...
         System.out.println("비밀번호 일치");
         
         System.out.println("member의 값은 "+member);
         model.addAttribute("member",member);
       
         return "mypage/member_info";
      } else {
         System.out.println("비밀번호 불일치");

         rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");

         return "redirect:/pw_confirm";
      }

   }

   // 마이페이지 - 회원탈퇴 (링크이동)
   @RequestMapping(value = "/member_out", method = RequestMethod.GET)
   public String memberOut() {
      return "mypage/member_out";
   }

   // 마이페이지 - 회원탈퇴 (컨트롤러)

   @RequestMapping(value = "/member_delete", method = RequestMethod.GET)
   public String memberOutController(MemberVO vo, RedirectAttributes rttr, HttpSession session) {
   

      String m_email = (String) session.getAttribute("m_email");
      String m_password = (String) session.getAttribute(vo.getM_password());
      int id = (int)session.getAttribute("id");

      System.out.println("m_email:" + m_email);
      System.out.println("m_password:" + m_password);
      System.out.println("id:" + id);

      rttr.addFlashAttribute("delete_msg", "탈퇴가 왼료되었습니다.");

      // 로그아웃으로 세션들을 초기화시킴
      session.invalidate();

      return "redirect:/";
   }
   
   

   // 마이페이지 - 1:1 문의내역 리스트
   @RequestMapping(value = "/one_list", method = RequestMethod.GET)
   public String oneList(Model model,@ModelAttribute("searchCriteria") 
	SearchCriteria searchCriteria,HttpServletRequest request, HttpSession session) {

    
      // 사용자 정보
      String m_email = (String) session.getAttribute("m_email");
      String m_name = myPageService.getMemberName(m_email); // System.out.println("=============MyPageController.java=====================
                                                // m_name : " + m_name);
      request.setAttribute("m_name", m_name);

      // 1:1 문의내역
//      List<OneVO> qnaList = null;
//      int id = myPageService.getMemberId(m_email); // System.out.println("=============MyPageController.java=====================
//                                          // id : " + id);
//      qnaList = myPageService.getQnaList(id);
//      request.setAttribute("qnaList", qnaList);
      
      PageMaker pageMaker = new PageMaker();
      pageMaker.setCriteria(searchCriteria);
      pageMaker.setTotalCount(myPageService.countSearchedArticles(searchCriteria));
      model.addAttribute("qnaList", myPageService.listSearch(searchCriteria));
      System.out.println("pageMaker="+pageMaker);
      model.addAttribute("pageMaker", pageMaker);
      
      
      return "mypage/one_list";
   }

   // 마이페이지 - 1:1 문의내역 리스트 - 1:1문의내역 등록
   @RequestMapping(value = "/one_register", method = RequestMethod.GET)
   public String oneRegister(HttpServletRequest request, HttpSession session, OneVO oneVO) {

      // 사용자 정보
      String m_email = (String) session.getAttribute("m_email");
      String m_name = myPageService.getMemberName(m_email);
      String m_nickname = myPageService.getMemberNickname(m_email); // System.out.println("=============MyPageController.java=====================
                                                      // nickname : " + m_nickname);
      request.setAttribute("m_name", m_name);
      request.setAttribute("m_nickname", m_nickname);

//      myPageService.insertQna(oneVO);

      return "mypage/one_register";
   }

   // 마이페이지 - 1:1 문의내역 리스트 - 1:1문의내역 등록 액션
   @RequestMapping(value = "/one_registerAction", method = RequestMethod.POST)
   public String oneRegisterAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
         OneVO oneVO) {

  

      oneVO.setId(myPageService.getMemberId((String) session.getAttribute("m_email")));

      // qna_title, qna_content의 앞뒤 공백 제거
      oneVO.setQna_title(oneVO.getQna_title().trim());
      oneVO.setQna_content(oneVO.getQna_content().trim());

      try {
         int result = myPageService.insertQna(oneVO);
         if (result == 0) {
            return "redirect:/one_register";
         }
      } catch (Exception e) {
         System.out.println("ERROR : oneRegisterAction - " + e.getMessage());
      }
      return "redirect:/one_list";

   }
   
   
   

   // 마이페이지 - 1:1 문의내역 수정
   @RequestMapping(value = "/one_update", method = RequestMethod.GET)
   public String oneUpdate(HttpSession session, HttpServletRequest request) {
   
      String m_email = (String) session.getAttribute("m_email");
      String m_name = myPageService.getMemberName(m_email);
      String m_nickname = myPageService.getMemberNickname(m_email);

      // 사용자의 id를 가져옴
      int id = myPageService.getMemberId(m_email);

      // qna_no=?의 작성자와 일치하는지 확인 후 일치하면 수정페이지로, 불일치하면 리스트로
      int qna_no = Integer.parseInt(request.getParameter("qna_no"));
      OneVO qnaDetail = myPageService.getQnaDetail(qna_no);

      if (id != qnaDetail.getId()) {
         return "redirect:/one_list";
      }

      request.setAttribute("m_name", m_name);
      request.setAttribute("m_nickname", m_nickname);
      request.setAttribute("qnaDetail", qnaDetail);

      return "mypage/one_update";
   }

   // 마이페이지 - 1:1 문의내역 수정 액션
   @RequestMapping(value = "/one_updateAction", method = RequestMethod.POST)
   public String oneUpdateAction(HttpSession session, HttpServletRequest request, OneVO oneVO) {

  
      // qna_title, qna_content의 앞뒤 공백 제거
      oneVO.setQna_title(oneVO.getQna_title().trim());
      oneVO.setQna_content(oneVO.getQna_content().trim());

      try {
         int result = myPageService.updateQna(oneVO);
         if (result == 0) {
            return "redirect:/one_update?qna_no=" + oneVO.getQna_no();
         }
      } catch (Exception e) {
         System.out.println("ERROR : oneUpdateAction - " + e.getMessage());
      }
      return "redirect:/one_get?qna_no=" + oneVO.getQna_no();
   }

   // hm | 마이페이지 - FAQ
   @RequestMapping(value = "/faq", method = RequestMethod.GET)
   public String faqList(HttpSession session, HttpServletRequest request, HttpServletResponse response) {

      // 1. 좌측 사용자 정보
      String m_email = (String) session.getAttribute("m_email");
      String m_name = myPageService.getMemberName(m_email); // System.out.println("=============MyPageController.java=====================
      // m_name : " + m_name);
      request.setAttribute("m_name", m_name);

      return "mypage/faq";
   }

   // hm | 마이페이지 - one_get
   @RequestMapping(value = "/one_get", method = RequestMethod.GET)
   public String oneGet(HttpSession session, HttpServletRequest request, OneVO oneVO) {
      // 1. 사용자 정보
      String m_email = (String) session.getAttribute("m_email");
      String m_name = myPageService.getMemberName(m_email);
      String m_nickname = myPageService.getMemberNickname(m_email);

      // 2. 문의내용
     

      int qna_no = Integer.parseInt(request.getParameter("qna_no"));
      OneVO qnaDetail = myPageService.getQnaDetail(qna_no);

      request.setAttribute("m_name", m_name);
      request.setAttribute("m_nickname", m_nickname);
      request.setAttribute("qnaDetail", qnaDetail);

      // 3. 답변내용
      OneAdVO qnaAdDetail = myPageService.getQnaAdDetail(qna_no);
      request.setAttribute("qnaAdDetail", qnaAdDetail);

      return "mypage/one_get";
   }

   
   // qna 삭제용
   @RequestMapping(value = "/one_delete", method = RequestMethod.GET)
   public String deleteQna(int qna_no) {
	
	   myPageService.deleteQna(qna_no);
	   
	   return "mypage/one_list";
	   
   }
   
   //팔로잉 팔로워 더보기
   @RequestMapping(value = "/folfol_list", method = RequestMethod.GET)
   public String folfol_list(Model model,@ModelAttribute("searchCriteria") 
	SearchCriteria searchCriteria,HttpServletRequest request, HttpSession session) {
	   
	   int id = myPageService.getMemberId((String) session.getAttribute("m_email"));
	      MemberVO member = (MemberVO) myPageService.getMember(id);
	      model.addAttribute("member", member);
	      
	      
	      List<folfolDTO> followerList = null;
	      followerList = myPageService.getfollower(id);
	      model.addAttribute("follower", followerList);
	      System.out.println(followerList);
	      
	      List<folfolDTO> followingList = null;
	      followingList = myPageService.getfollowing(id);
	      model.addAttribute("following", followingList);
	      System.out.println(followingList);
	
	   
	   return "mypage/folfol_list";
	   
   }
   
   

}