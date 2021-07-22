package org.hdcd.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.hdcd.common.domain.CodeLabelValue;
import org.hdcd.common.domain.PageRequest;
import org.hdcd.common.domain.Pagination;
import org.hdcd.common.security.domain.CustomUser;
import org.hdcd.domain.Board;
import org.hdcd.domain.Member;
import org.hdcd.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService service;

	@Value("${upload.path}")
	private String uploadPath;
	
	@GetMapping( "/register")
	@PreAuthorize("hasRole('ROLE_MEMBER')") //멤버 권한만 가진 사용자만 접근 가능
	public void registerForm(Model model, Authentication authentication) throws Exception {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Member member = customUser.getMember();

		Board board = new Board();

		board.setWriter(member.getUserId());

		model.addAttribute(board);
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String register(Board board, RedirectAttributes rttr) throws Exception {
		service.register(board);

		rttr.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/board/list";
	}


	//페이지요청 정보를 매개변수로 받고 다시 뷰에 전달한다.
	@GetMapping("/list")
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

	
	
	//페이징요청 정보를 매개변수로 받고 다시 뷰에 전달한다.
	@GetMapping("/read")
	public void read(int boardNo, @ModelAttribute("pgrq") PageRequest pageRequest, Model model) throws Exception {
		
		//조회한 게시글 상세정보를 뷰에 전달한다.
		Board board = service.read(boardNo);
	
		//board.setPageRequest(pageRequest);
		model.addAttribute(board);
	}

	@PostMapping("/remove")
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

	@GetMapping("/modify")
	@PreAuthorize("hasAnyRole('ROLE_MEMBER')")
	//페이징요청 정보를 매개변수로 받고 다시 뷰로 전달한다.
	public void modifyForm(int boardNo, @ModelAttribute("pgrq") PageRequest pageRequest, Model model) throws Exception {
		
		//조회한 게시글 상세정보를 뷰에 전달한다.
		Board board = service.read(boardNo);
	
		//board.setPageReqest(pageRequest);
		
		model.addAttribute(board);
	}

	@PostMapping("/modify")
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
	
	
	@ResponseBody
	@PostMapping("/ajax/test")
	public Map<String, Object> ajaxtest(String test) {
		System.out.println("값 : " + test);
		Map<String, Object> result = new HashMap<>();
		result.put("result","OK");
		return result;
	}
	
	
	//produces는 HTTP 응답 헤더로 "Content-Type: application/json;charset=UTF-8"을 반환한다
	@PostMapping(value="/uploadSummernoteImageFile", produces = "application/json")
	@ResponseBody
	public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
		System.out.println("이미지 업로드!!!!!");
		JsonObject jsonObject = new JsonObject();
		
		String fileRoot = "C:/summernote_image/";	//저장될 외부 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
				
		//UUID.randomUUID 식별자 생성
		String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
		
		File targetFile = new File(fileRoot + savedFileName);	
		
		try {
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
			jsonObject.addProperty("url", "/summernoteImage/"+savedFileName);
			jsonObject.addProperty("responseCode", "success");
				
		} catch (IOException e) {
			FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
			jsonObject.addProperty("responseCode", "error");
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
}
	
	
	
	
	
	
	
	
	

