package org.hdcd.controller;

import java.util.ArrayList;
import java.util.List;

import org.hdcd.common.domain.CodeLabelValue;
import org.hdcd.common.domain.PageRequest;
import org.hdcd.common.domain.Pagination;
import org.hdcd.common.security.domain.CustomUser;
import org.hdcd.domain.Board;
import org.hdcd.domain.Member;
import org.hdcd.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService service;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_MEMBER')") //멤버 권한만 가진 사용자만 접근 가능
	public void registerForm(Model model, Authentication authentication) throws Exception {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Member member = customUser.getMember();

		Board board = new Board();

		board.setWriter(member.getUserId());

		model.addAttribute(board);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String register(Board board, RedirectAttributes rttr) throws Exception {
		service.register(board);

		rttr.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/board/list";
	}



	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//페이지요청 정보를 매개변수로 받고 다시 뷰에 전달한다.
	public void list(@ModelAttribute("pgrq") PageRequest pageRequest, Model model) throws Exception {
		
		//뷰에 페이징 처리를 한 게시글 목록을 전달한다.
		model.addAttribute("list", service.list(pageRequest));
		
		/*	
		 페이징 네비게이션 정보를 뷰에 전달한다 
		 package org.hdcd.common.domain;
		 public class Pagination 
		 */
		Pagination pagination = new Pagination();
		pagination.setPageRequest(pageRequest);
		
		//페이지 네비게이션 정보에 검색처리된 게시글 건수를 지정한다.
		pagination.setTotalCount(service.count(pageRequest));
		
		
		model.addAttribute("pagination", pagination);
		
		
		List<CodeLabelValue> searchTypeCodeValueList = new ArrayList<CodeLabelValue>();
	
		//검색 유형의 코드명과 코드값을 정의한다.
		searchTypeCodeValueList.add(new CodeLabelValue("title", "제목"));
		searchTypeCodeValueList.add(new CodeLabelValue("content", "내용"));
		searchTypeCodeValueList.add(new CodeLabelValue("writer", "작성자"));
	
		model.addAttribute("searchTypeCodeValueList", searchTypeCodeValueList);
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	
	//페이징요청 정보를 매개변수로 받고 다시 뷰에 전달한다.
	public void read(int boardNo, @ModelAttribute("pgrq") PageRequest pageRequest, Model model) throws Exception {
		
		//조회한 게시글 상세정보를 뷰에 전달한다.
		Board board = service.read(boardNo);
	
		//board.setPageRequest(pageRequest);
		model.addAttribute(board);
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_MEMBER')")
	//페이징요청 정보를 매개변수로 받고 다시 뷰에 전달한다.
	public String remove(int boardNo, PageRequest pageRequest, RedirectAttributes rttr) throws Exception {
		service.remove(boardNo);
		
		//RedirectAttributes 객체에 일회성 데이터를 지정하여 전달한다.
		rttr.addAttribute("page", pageRequest.getPage());
		rttr.addAttribute("sizePerPage", pageRequest.getSizePerPage());
		
		//검색유형과 검색어를 뷰로 전달한다.
		rttr.addAttribute("searchType", pageRequest.getSearchType());
		rttr.addAttribute("keyword", pageRequest.getKeyword());

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/list";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_MEMBER')")
	//페이징요청 정보를 매개변수로 받고 다시 뷰로 전달한다.
	public void modifyForm(int boardNo, @ModelAttribute("pgrq") PageRequest pageRequest, Model model) throws Exception {
		
		//조회한 게시글 상세정보를 뷰에 전달한다.
		Board board = service.read(boardNo);
	
		//board.setPageReqest(pageRequest);
		
		model.addAttribute(board);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_MEMBER')")
	//페이징요청 정보를 매개변수로 받고 다시 뷰에 전달한다.
	public String modify(Board board, PageRequest pageRequest, RedirectAttributes rttr) throws Exception {
		service.modify(board);
		
		//RedirectAttributes 객체에 일회성 데이터를 지정하여 전달한다.
		rttr.addAttribute("page", pageRequest.getPage());
		rttr.addAttribute("sizePerPage", pageRequest.getSizePerPage());
		rttr.addAttribute("searchType", pageRequest.getSearchType());
		rttr.addAttribute("keyword", pageRequest.getKeyword());
	    
		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/list";
	}
	
	
}
