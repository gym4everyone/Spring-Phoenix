package com.kh.phoenix.board;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardLogic {
	Logger logger = LogManager.getLogger(BoardLogic.class);
	@Autowired
	private BoardDao boardDao = null;
	
	public List<Map<String, Object>> boardList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.boardList(pMap);
		return list;
	}

	public int boardInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.boardInsert(pMap);
		return result;
	}
	

	public int fileUpdate(List<Map<String, Object>> pList) {
		return boardDao.fileUpdate(pList);
	}

	public int fileInsert(Map<String, Object> pMap) {
		return boardDao.fileInsert(pMap);
	}

	public List<Map<String, Object>> boardDetail(Map<String, Object> pMap) {
		return boardDao.boardDetail(pMap);
	}

	public List<Map<String, Object>> fileList(Map<String, Object> pMap) {
		return boardDao.fileList(pMap);
	}

	public int boardUpdate(Map<String, Object> map) {
		return boardDao.boardUpdate(map);
	}

	public int fileDelete(Map<String, Object> map) {
		return boardDao.fileDelete(map);
	}

	public int boardDelete(Map<String, Object> map) {
		return boardDao.boardDelete(map);
	}
	

	/*
	public List<Map<String, Object>> masterList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.masterList(pMap);
		return list;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<Map<String, Object>> masterDetail(Map<String, Object> pMap,int master_bno)throws Exception {
		List<Map<String, Object>> list = null;
		boardDao.masterHitUpdate(master_bno);
		list = boardDao.masterDetail(pMap);
		return list;
	}
	
	public int masterInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.masterInsert(pMap);
		return result;
	}
	
	public int masterUpdate(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.masterUpdate(pMap);
		return result;
	}
	
	public int masterDelete(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.masterDelete(pMap);
		return result;
	}
	*/
	
	public List<Map<String, Object>> qnaList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.qnaList(pMap);
		return list;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<Map<String, Object>> qnaDetail(Map<String, Object> pMap,int qna_bno) throws Exception {
		List<Map<String, Object>> list = null;
		boardDao.qnaHitUpdate(qna_bno);
		list = boardDao.qnaDetail(pMap);
		return list;
	}
	
	public int qnaInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.qnaInsert(pMap);
		return result;
	}
	
	public int qnaUpdate(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.qnaUpdate(pMap);
		return result;
	}
	
	public int qnaDelete(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.qnaDelete(pMap);
		return result;
	}
	
	public List<Map<String, Object>> qcList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.qcList(pMap);
		return list;
	}
	
	public int qcInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.qcInsert(pMap);
		return result;
	}
	
	public int qcUpdate(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.qcUpdate(pMap);
		return result;
	}
	
	public int qcDelete(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.qcDelete(pMap);
		return result;
	}
	
	public List<Map<String, Object>> reviewList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.reviewList(pMap);
		return list;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<Map<String, Object>> reviewDetail(Map<String, Object> pMap,int review_bno) throws Exception {
		List<Map<String, Object>> list = null;
		boardDao.reviewHitUpdate(review_bno);
		list = boardDao.reviewDetail(pMap);
		return list;
	}
	
	public int reviewInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.reviewInsert(pMap);
		return result;
	}
	
	public int reviewUpdate(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.reviewUpdate(pMap);
		return result;
	}
	
	public int reviewDelete(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.reviewDelete(pMap);
		return result;
	}
	
	public List<Map<String, Object>> rcList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.rcList(pMap);
		return list;
	}
	
	public int rcInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.rcInsert(pMap);
		return result;
	}
	
	public int rcUpdate(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.rcUpdate(pMap);
		return result;
	}
	
	public int rcDelete(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.rcDelete(pMap);
		return result;
	}
	
	public List<Map<String, Object>> transBList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.transBList(pMap);
		return list;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<Map<String, Object>> transBDetail(Map<String, Object> pMap,int transB_bno) throws Exception {
		List<Map<String, Object>> list = null;
		boardDao.transBHitUpdate(transB_bno);
		list = boardDao.transBDetail(pMap);
		return list;
	}


	public int transBInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.transBInsert(pMap);
		return result;
	}

	public int transBUpdate(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.transBUpdate(pMap);
		return result;
	}

	public int transBDelete(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.transBDelete(pMap);
		return result;
	}
	
	public List<Map<String, Object>> tcList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.tcList(pMap);
		return list;
	}

	public int tcInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.tcInsert(pMap);
		return result;
	}

	public int tcUpdate(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.tcUpdate(pMap);
		return result;
	}

	public int tcDelete(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.tcDelete(pMap);
		return result;
	}

	public List<Map<String, Object>> commentersList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.commentersList(pMap);
		return list;
	}

	public int transferInsert(Map<String, Object> pMap) {
		int result = 0;
		result = boardDao.transferInsert(pMap);
		return result;
	}

	public List<Map<String, Object>> myBoardList(Map<String, Object> pMap) {
		List<Map<String, Object>> list = null;
		list = boardDao.myBoardList(pMap);
		return list;
	}

	
}
