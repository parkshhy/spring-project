package org.hdcd.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.hdcd.domain.Item;
import org.hdcd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/item")
public class ItemController {

	//
	@Autowired
	private ItemService itemService;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@GetMapping("/register")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String registerForm(Model model) {
		
		model.addAttribute(new Item());
		
		return "item/register";
	}
	
	@PostMapping("/register")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String register(Item item, RedirectAttributes rttr) throws Exception {
		MultipartFile pictureFile = item.getPicture();
		MultipartFile previewFile = item.getPreview();
		
		String createdPictureFilename = uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());
		String createdPreviewFilename = uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());

		item.setPictureUrl(createdPictureFilename);
		item.setPreviewUrl(createdPreviewFilename);

		itemService.register(item);

		rttr.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/item/list";
	}
	
	private String uploadFile(String originalName, byte[] fileData) throws Exception {
		UUID uid = UUID.randomUUID();

		String createdFileName = uid.toString() + "_" + originalName;

		File target = new File(uploadPath, createdFileName);

		FileCopyUtils.copy(fileData, target);

		return createdFileName;
	}
	
	@GetMapping("/list")
	public void list(Model model) throws Exception{
		List<Item> itemList = itemService.list();
		
		model.addAttribute("itemList",itemList);
		
	}
	
	@GetMapping("/read")
	public String read(int itemId,Model model) throws Exception{
		Item item = itemService.read(itemId);
		
		model.addAttribute(item);
		
		return "item/read";
	}

	
	//수정화면 읽기
	@GetMapping("/modify")
	@PreAuthorize("hasRole('ROLE_MEMBER')") //회원만 접근 가능
	public String modiftyForm(int itemId,Model model) throws Exception{
		
		Item item = itemService.read(itemId);
		
		model.addAttribute(item);
		
		return "item/modify";
		
	}
	@PostMapping("/modify")
	@PreAuthorize("hasRole('ROLE_MEMBER')") 
	public String modify(Item item, RedirectAttributes rttr) throws Exception {
		MultipartFile pictureFile = item.getPicture();

		if (pictureFile != null && pictureFile.getSize() > 0) {
			String createdFilename = uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());

			item.setPictureUrl(createdFilename);
		}
		
		MultipartFile previewFile = item.getPreview();

		if (previewFile != null && previewFile.getSize() > 0) {
			String createdFilename = uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());

			item.setPreviewUrl(createdFilename);
		}

		itemService.modify(item);

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/item/list";
	}
	
	
	@GetMapping("/remove")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String removeForm(int itemId,Model model) throws Exception{
		Item item = itemService.read(itemId);
		
		model.addAttribute(item);
		
		return "item/remove";
		
	}
	
	@PostMapping("/remove")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String remove(Item item,RedirectAttributes rttr) throws Exception{
		
		itemService.remove(item.getItemId());
		
		rttr.addFlashAttribute("msg","SUCESS");
		
		return "redirect:/item/list";
		
	}
	
	//미리보기 이미지 표시하기

	@ResponseBody
	@RequestMapping("/display")
	public ResponseEntity<byte[]> displayFile(Integer itemId) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		String fileName = itemService.getPreview(itemId);

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

			MediaType mType = getMediaType(formatName);

			HttpHeaders headers = new HttpHeaders();

			in = new FileInputStream(uploadPath + File.separator + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			}

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}
		
	private MediaType getMediaType(String formatName){
		if(formatName != null) {
			if(formatName.equals("JPG")) {
				return MediaType.IMAGE_JPEG;
			}
			
			if(formatName.equals("GIF")) {
				return MediaType.IMAGE_GIF;
			}
			
			if(formatName.equals("PNG")) {
				return MediaType.IMAGE_PNG;
			}
		}
		
		return null;
	}
}