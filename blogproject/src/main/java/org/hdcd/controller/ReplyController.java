package org.hdcd.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hdcd.domain.Reply;
import org.hdcd.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//@ResponseBody를 붙이지 않아도 뷰가 아닌 데이터 리턴 가능
@RestController
@RequestMapping("reply/*") //공통적인 url pattern
public class ReplyController {

	 @Autowired       //서비스를 호출하기위해서 의존성을 주입
	 private ReplyService replyService;
	 
	 
	 //댓글리스트를 호출할때 맵핑되는 메소드
	 @RequestMapping("list.do")
	 public ModelAndView list(int board_no, ModelAndView mav) throws Exception {
	     List<Reply> list=replyService.list(board_no); //댓글 목록
	     mav.setViewName("board/reply_list"); //뷰의 이름
	     mav.addObject("list", list); //뷰에 전달할 데이터 저장
	     return mav; //뷰로 이동
	 }
	 
	 
	 //댓글 목록을 ArrayList로 리턴
	 @RequestMapping("list_json.do")
	 public List<Reply> list_json(int board_no) throws Exception{
	     return replyService.list(board_no);
	 }
	 
	 
	 @RequestMapping("insert.do") //세부적인 url pattern
	 public void insert(Reply reply, HttpSession session) throws Exception{
	     
	     //댓글 작성자 아이디
	     //현재 접속중인 사용자 아이디
	     String userid=(String)session.getAttribute("user_id");
	     reply.setReplyer(userid);
	     
	     //댓글이 테이블에 저장됨
	     replyService.create(reply);
	     //jsp 페이지로 가거나 데이터를 리턴하지 않음
	 }
}
