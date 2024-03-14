/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.com.fof.co.sp.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.fof.co.mn.service.impl.MnMapper;
import egovframework.com.fof.co.sp.service.impl.SpMapper;
import egovframework.com.fof.utl.CommonUtil;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
public class SpController {

	@Resource(name = "spMapper")
	private SpMapper spMapper;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	// System 관련하여 생성 규칙에 따른 설계가 필요함

	/**
	 * 샘플 전용 조회
	 * @param model
	 * @return layout
	 * @exception Exception
	 */
	@RequestMapping(value = "/sampleList")
	public String selectSampleList(ModelMap model) throws Exception {
		
		List<EgovMap> selectSpList = spMapper.selectSpList();
		
		model.addAttribute("selectSpList", selectSpList);
		model.addAttribute("content", "co/sp/sampleList.jsp");
		
		return CommonUtil.getSubLayoutPathBySysID("test");
	}

}
