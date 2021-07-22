package org.hdcd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hdcd.common.util.UploadFileUtils;
import org.hdcd.domain.Pds;
import org.hdcd.service.PdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pds")
public class PdsController {

	@Autowired
	private PdsService pdsService;

	@Value("${upload.path}")
	private String uploadPath;

	@GetMapping("/list")
	public void list(Model model) throws Exception {
		List<Pds> itemList = this.pdsService.list();

		model.addAttribute("itemList", itemList);
	}

	@GetMapping("/register")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String registerForm(Model model) {
		model.addAttribute(new Pds());

		return "pds/register";
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String register(Pds pds, RedirectAttributes rttr) throws Exception {
		this.pdsService.register(pds);

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/pds/list";
	}
	
	@GetMapping("/read")
	public String read(Integer itemId, Model model) throws Exception {
		Pds pds = this.pdsService.read(itemId);

		model.addAttribute(pds);

		return "pds/read";
	}

	@GetMapping("/modify")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String modifyForm(Integer itemId, Model model) throws Exception {
		Pds pds = this.pdsService.read(itemId);

		model.addAttribute(pds);

		return "pds/modify";
	}

	@PostMapping("/modify")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String modify(Pds pds, RedirectAttributes rttr) throws Exception {
		this.pdsService.modify(pds);

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/pds/list";
	}

	@GetMapping("/remove")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String removeForm(Integer itemId, Model model) throws Exception {
		Pds pds = this.pdsService.read(itemId);

		model.addAttribute(pds);

		return "pds/remove";
	}

	@PostMapping("/remove")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public String remove(Pds pds, RedirectAttributes rttr) throws Exception {
		this.pdsService.remove(pds.getItemId());

		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/pds/list";
	}

	@ResponseBody
	@RequestMapping(value = "/uploadAjax", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {
		String savedName = UploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());

		return new ResponseEntity<String>(savedName, HttpStatus.CREATED);
	}

	@ResponseBody
	@RequestMapping("/downloadFile")
	public ResponseEntity<byte[]> downloadFile(String fullName) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		pdsService.updateAttachDownCnt(fullName);

		try {
			HttpHeaders headers = new HttpHeaders();

			in = new FileInputStream(uploadPath + fullName);

			String fileName = fullName.substring(fullName.indexOf("_") + 1);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		
		return entity;
	}

	@ResponseBody
	@PostMapping("/deleteFile")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public ResponseEntity<String> deleteFile(String fileName) {
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();

		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping("/getAttach/{itemId}")
	public List<String> getAttach(@PathVariable("itemId") Integer itemId) throws Exception {
		return pdsService.getAttach(itemId);
	}

}
