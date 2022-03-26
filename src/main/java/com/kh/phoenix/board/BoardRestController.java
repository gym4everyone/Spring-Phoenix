package com.kh.phoenix.board;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;

@RestController
@RequestMapping("/board/*")
public class BoardRestController {
	Logger logger = LogManager.getLogger(BoardRestController.class);

	@Autowired
	private BoardLogic boardLogic = null;
	
	@GetMapping("boardList")
	public String boardList(@RequestParam Map<String,Object> pMap) {
		logger.info("boardList 호출 성공");
		List<Map<String,Object>> boardList = null;
		//total = boardLogic.totalRecord(pMap);
		boardList = boardLogic.boardList(pMap);
		logger.info("boardList : " + boardList);
		String result = null;
		Gson g = new Gson(); 
		result = g.toJson(boardList);
		return result;
	}// end of boardList
	
	@GetMapping("boardDetail")
	public String boardDetail(@RequestParam Map<String,Object> pMap) {
		logger.info("boardDetail 호출 성공");
		List<Map<String,Object>> boardDetail = null;
		//total = boardLogic.totalRecord(pMap);
		boardDetail = boardLogic.boardDetail(pMap);
		List<Map<String, Object>> fileList = boardLogic.fileList(pMap);
		boardDetail.addAll(fileList);
		logger.info("boardDetail : " + boardDetail);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(boardDetail);
		return result;
	}// end of boardList
	
	
	@PostMapping("boardInsert")
	public String boardInsert(@RequestBody Map<String,Object> map) {
		logger.info("boardInsert 호출 성공");
		logger.info(map);

		int result = 0;		
		result = boardLogic.boardInsert(map);
		//첨부파일이 존재하나?
		if(map.get("fileNames")!=null&&result!=0) {
			//tablename, bno, filename
			List<Map<String, Object>> pList = new ArrayList<Map<String,Object>>();
			Map<String, Object> pMap = null;
			String[] fileNames = map.get("fileNames").toString().substring(1,map.get("fileNames").toString().length()-1).split(", "); 
			for(String name : fileNames) {
				logger.info(name);
				pMap = new HashMap<String, Object>();
				pMap.put("name", name);
				pMap.put("bno", map.get("master_bno"));
				pMap.put("id", map.get("id"));
				pList.add(pMap);
			}
			result = boardLogic.fileUpdate(pList);
		}
		
		return String.valueOf(result);
	}
	
	
	
	@PostMapping("boardUpdate")
	public String boardUpdate(@RequestBody Map<String,Object> map) {
		logger.info("boardInsert 호출 성공");
		logger.info(map);
		
		int result = 0;		
		result = boardLogic.boardUpdate(map);
		//첨부파일이 존재하나?
		if(map.get("fileNames")!=null&&result!=0) {
			//tablename, bno, filename
			List<Map<String, Object>> pList = new ArrayList<Map<String,Object>>();
			Map<String, Object> pMap = null;
			String[] fileNames = map.get("fileNames").toString().substring(1,map.get("fileNames").toString().length()-1).split(", "); 
			for(String name : fileNames) {
				logger.info(name);
				pMap = new HashMap<String, Object>();
				pMap.put("name", name);
				pMap.put("bno", map.get("bno"));
				pMap.put("id", map.get("id"));
				pList.add(pMap);
			}			
			result = boardLogic.fileDelete(map);
			result = boardLogic.fileUpdate(pList);
		}
		
		return String.valueOf(result);
	}
	
	
	@PostMapping("boardDelete")
	public String boardDelete(@RequestBody Map<String,Object> map) {
		logger.info("boardDelete 호출 성공");
		logger.info(map);
		
		int result = 0;		
		result = boardLogic.boardDelete(map);
		
		return String.valueOf(result);
	}
	
	
	
	@PostMapping("imageUpload")
	public String imageUpload(@RequestParam(value="image") MultipartFile image) {
		Map<String, Object> pMap = new HashMap<String, Object>();
		logger.info("imageUpload 호출 성공");
		logger.info("image:"+image);
		String savePath =  "C:\\JANG\\CODE\\Coding\\Workspace\\eclipse-workspace_eGov\\spring-phoenix\\src\\main\\webapp\\file";
		String filename =  null;
		String fullPath = null;
		int result = 0;		

		//첨부파일이 존재하나?
		if(image!=null && !image.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar time = Calendar.getInstance();
			filename = sdf.format(time.getTime())+'-'+image.getOriginalFilename().replaceAll(" ", "-");
			fullPath = savePath+"\\"+filename;
			try {
				logger.info("fullPath : "+fullPath);
				File file = new File(fullPath);//파일명만 존재하고 내용은 없는
				byte[] bytes = image.getBytes();
				BufferedOutputStream bos = 
						new BufferedOutputStream(new FileOutputStream(file));
				//52번에서 생성된 File객체에 내용쓰기
				bos.write(bytes);
				bos.close();
				//파일크기
				double size = Math.floor(file.length()/(1024.0*1024.0)*10)/10;
				logger.info("size : "+size);
				pMap.put("name",filename);
				pMap.put("size",size);
				pMap.put("path",fullPath);
				
				result = boardLogic.fileInsert(pMap);
			}catch(Exception e) {		
				e.printStackTrace();
			}
		}		
		logger.info(filename);
		return filename;
	}// end of boardList
	
	
	@GetMapping("imageDownload")
	public byte[] imageDownload(@RequestParam(value="imageName") String imageName) {
		String fname = null;
		try {
			fname = URLDecoder.decode(imageName, "UTF-8");
			logger.info(fname);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//out.print("b_file: 8->euc"+b_file);		
		//out.print("<br>");		
		String filePath = "C:\\JANG\\CODE\\Coding\\Workspace\\eclipse-workspace_eGov\\spring-phoenix\\src\\main\\webapp\\file"; // 절대경로.	
		//가져온 파일이름을 객체화 시켜줌. - 파일이 있는 물리적인 경로가 필요함.
		File file = new File(filePath, fname.trim());
	   	
	 	//해당 파일을 읽어오는 객체 생성해 줌. - 이 때 파일명을 객체화 한 주소번지가 필요함. 
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try{
            fis = new FileInputStream(file);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try{
            while((readCount = fis.read(buffer)) != -1){
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch(IOException e){
            throw new RuntimeException("File Error");
        }
        return fileArray;
	}// end of boardList
	
	
	
	/*
	@GetMapping("masterList")
	public String masterList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("masterList 호출");
		list = boardLogic.masterList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}
	
	@GetMapping("masterDetail")
	public String masterDetail(@RequestParam Map<String,Object> pMap, int master_bno) throws Exception {
		List<Map<String,Object>> masterDetail = null;
		logger.info("boardDetail 호출");
		masterDetail = boardLogic.masterDetail(pMap, master_bno);//조건검색가능
		String result = null;
		Gson g = new Gson();
		result = g.toJson(masterDetail);		
		return result;
	} 

	@PostMapping("masterInsert")
	public int masterInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("masterInsert 호출");
		int result = 0;
		result = boardLogic.masterInsert(pMap);

		return result;
	}
	
	@PostMapping("masterUpdate")
	public int masterUpdate(@RequestParam Map<String, Object> pMap) {
		logger.info("masterUpdate 호출");
		int result = 0;
		result = boardLogic.masterUpdate(pMap);
		return result;
	}
	
	@PostMapping("masterDelete")
	public int masterDelete(@RequestParam Map<String, Object> pMap) {
		logger.info("masterDelete 호출");
		int result = 0;
		result = boardLogic.masterDelete(pMap);
		return result;
	}	
	*/
	
	@GetMapping("qnaList")
	public String qnaList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("qnaList 호출");
		list = boardLogic.qnaList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}
	
	@GetMapping("qnaDetail")
	public String qnaDetail(@RequestParam Map<String,Object> pMap,int qna_bno) throws Exception {
		List<Map<String,Object>> qnaDetail = null;
		logger.info("qnaDetail 호출");
		qnaDetail = boardLogic.qnaDetail(pMap,qna_bno);//조건검색가능
		String result = null;
		Gson g = new Gson();
		result = g.toJson(qnaDetail);		
		return result;
	}
	
	@PostMapping("qnaInsert")
	public int qnaInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("qnaInsert 호출");
		int result = 0;
		result = boardLogic.qnaInsert(pMap);
		
		return result;
	}
	
	@PostMapping("qnaUpdate")
	public int qnaUpdate(@RequestParam Map<String, Object> pMap) {
		logger.info("qnaUpdate 호출");
		int result = 0;
		result = boardLogic.qnaUpdate(pMap);
		return result;
	}
	
	@PostMapping("qnaDelete")
	public int qnaDelete(@RequestParam Map<String, Object> pMap) {
		logger.info("qnaDelete 호출");
		int result = 0;
		result = boardLogic.qnaDelete(pMap);
		return result;
	}	
	
	@GetMapping("qcList")
	public String qcList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("qcList 호출");
		list = boardLogic.qcList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}
	
	@PostMapping("qcInsert")
	public int qcInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("qcInsert 호출");
		int result = 0;
		result = boardLogic.qcInsert(pMap);		
		return result;
	}
	
	@PostMapping("qcUpdate")
	public int qcUpdate(@RequestParam Map<String, Object> pMap) {
		logger.info("qcUpdate 호출");
		int result = 0;
		result = boardLogic.qcUpdate(pMap);
		return result;
	}
	
	@PostMapping("qcDelete")
	public int qcDelete(@RequestParam Map<String, Object> pMap) {
		logger.info("qcDelete 호출");
		int result = 0;
		result = boardLogic.qcDelete(pMap);
		return result;
	}	
	
	@GetMapping("reviewList")
	public String reviewList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("reviewList 호출");
		list = boardLogic.reviewList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}
	
	@GetMapping("reviewDetail")
	public String reviewDetail(@RequestParam Map<String,Object> pMap,int review_bno) throws Exception {
		List<Map<String,Object>> reviewDetail = null;
		logger.info("reviewDetail 호출");
		reviewDetail = boardLogic.reviewDetail(pMap,review_bno);//조건검색가능
		String result = null;
		Gson g = new Gson();
		result = g.toJson(reviewDetail);		
		return result;
	}
	
	@PostMapping("reviewInsert")
	public int reviewInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("reviewInsert 호출");
		int result = 0;
		result = boardLogic.reviewInsert(pMap);		
		return result;
	}
	
	@PostMapping("reviewUpdate")
	public int reviewUpdate(@RequestParam Map<String, Object> pMap) {
		logger.info("reviewUpdate 호출");
		int result = 0;
		result = boardLogic.reviewUpdate(pMap);
		return result;
	}
	
	@PostMapping("reviewDelete")
	public int reviewDelete(@RequestParam Map<String, Object> pMap) {
		logger.info("reviewDelete 호출");
		int result = 0;
		result = boardLogic.reviewDelete(pMap);
		return result;
	}	
	
	@GetMapping("rcList")
	public String rcList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("rcList 호출");
		list = boardLogic.rcList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}
	
	@PostMapping("rcInsert")
	public int rcInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("rcInsert 호출");
		int result = 0;
		result = boardLogic.rcInsert(pMap);		
		return result;
	}
	
	@PostMapping("rcUpdate")
	public int rcUpdate(@RequestParam Map<String, Object> pMap) {
		logger.info("rcUpdate 호출");
		int result = 0;
		result = boardLogic.rcUpdate(pMap);
		return result;
	}
	
	@PostMapping("rcDelete")
	public int rcDelete(@RequestParam Map<String, Object> pMap) {
		logger.info("rcDelete 호출");
		int result = 0;
		result = boardLogic.rcDelete(pMap);
		return result;
	}	
	
	
	@GetMapping("transBList")
	public String transBList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("transBList 호출");
		list = boardLogic.transBList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}

	@GetMapping("transBDetail")
	public String transBDetail(@RequestParam Map<String,Object> pMap,int transB_bno) throws Exception {
		List<Map<String,Object>> transBDetail = null;
		logger.info("transBDetail 호출");
		transBDetail = boardLogic.transBDetail(pMap,transB_bno);//조건검색가능
		String result = null;
		Gson g = new Gson();
		result = g.toJson(transBDetail);		
		return result;
	}
	
	@PostMapping("transBInsert")
	public int transBInsert(@RequestParam Map<String, Object> pMap) {

		logger.info("transBInsert 호출");
		int result = 0;
		result = boardLogic.transBInsert(pMap);

		return result;
	}
	
	@PostMapping("transBUpdate")
	public int transBUpdate(@RequestParam Map<String, Object> pMap) {
		logger.info("transBUpdate 호출");
		int result = 0;
		result = boardLogic.transBUpdate(pMap);
		return result;
	}
	
	@PostMapping("transBDelete")
	public int transBDelete(@RequestParam Map<String, Object> pMap) {
		logger.info("transBDelete 호출");
		int result = 0;
		result = boardLogic.transBDelete(pMap);
		return result;
	}
	
	@GetMapping("tcList")
	public String tcList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("rcList 호출");
		list = boardLogic.tcList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}
	
	@PostMapping("tcInsert")
	public int tcInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("tcInsert 호출");
		int result = 0;
		result = boardLogic.tcInsert(pMap);

		return result;
	}
	
	@PostMapping("tcUpdate")
	public int tcUpdate(@RequestParam Map<String, Object> pMap) {
		logger.info("tcUpdate 호출");
		int result = 0;
		result = boardLogic.tcUpdate(pMap);
		return result;
	}
	
	@PostMapping("tcDelete")
	public int tcDelete(@RequestParam Map<String, Object> pMap) {
		logger.info("tcDelete 호출");
		int result = 0;
		result = boardLogic.tcDelete(pMap);
		return result;
	}
	
	@PostMapping("commentersList")
	public String commentersList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		logger.info("commentersList 호출");
		list = boardLogic.commentersList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(list);
		return result;
	}
	
	@PostMapping("transferInsert")
	public int trasferInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("transferInsert 호출");
		int result = 0;
		result = boardLogic.transferInsert(pMap);

		return result;
	}
	
	@PostMapping("myBoardList")
	public String myBoardList(@RequestParam Map<String, Object> pMap) {
		List<Map<String, Object>> boardList = null;
		logger.info("boardList 호출");
		boardList = boardLogic.myBoardList(pMap);
		String result = null;
		Gson g = new Gson();
		result = g.toJson(boardList);
		return result;
	}
	
	@PostMapping("fileInsert")
	public int fileInsert(@RequestParam Map<String, Object> pMap) {
		logger.info("fileInsert 호출");
		int result = 0;
		result = boardLogic.fileInsert(pMap);
		return result;
	}
}
