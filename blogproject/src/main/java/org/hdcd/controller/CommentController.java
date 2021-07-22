package org.hdcd.controller;

import java.util.List;

import org.hdcd.domain.Comment;
import org.hdcd.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	CommentService commentservice;

	 	@RequestMapping("/list") //댓글 리스트
	    @ResponseBody
	    public List<Comment> mCommentServiceList(Model model,int board_no) throws Exception{
	        System.out.println("@@@ board_no: "+board_no);
	        return commentservice.commentListService(board_no);
	    }
	    
	    @PostMapping("insert") //댓글 작성 
	    @ResponseBody
	    public int mCommentServiceInsert(@RequestParam int board_no, @RequestParam String content) throws Exception{
	        System.out.println("댓글 작성");
	        Comment comment = new Comment();
	        comment.setBoard_no(board_no);
	        comment.setContent(content);
	        //로그인 기능을 구현했거나 따로 댓글 작성자를 입력받는 폼이 있다면 입력 받아온 값으로 사용
	        comment.setWriter("test");  
	        
	        return commentservice.commentInsertService(comment);
	    }
	    
	    @PutMapping("/update") //댓글 수정  
	    @ResponseBody
	    public int mCommentServiceUpdateProc(@RequestParam int cno, @RequestParam String content) throws Exception{
	        
	        Comment comment = new Comment();
	        comment.setCno(cno);
	        comment.setContent(content);
	        
	        return commentservice.commentUpdateService(comment);
	    }
	    
	    @DeleteMapping("/delete/{cno}") //댓글 삭제  
	    @ResponseBody
	    public int mCommentServiceDelete(@PathVariable int cno) throws Exception{
	        
	        return commentservice.commentDeleteService(cno);
	    }

}
