package com.viettel.vtskit.easypoi.view;

import com.viettel.vtskit.easypoi.def.NormalExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import com.viettel.vtskit.easypoi.poi.excel.ExcelExportUtil;
import com.viettel.vtskit.easypoi.poi.excel.entity.ExportParams;
import com.viettel.vtskit.easypoi.poi.excel.export.ExcelExportServer;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Entity Entity data object export
 *
 */
@SuppressWarnings("unchecked")
@Controller(NormalExcelConstants.JEECG_ENTITY_EXCEL_VIEW)
public class JeecgEntityExcelView extends MiniAbstractExcelView {

	public JeecgEntityExcelView() {
		super();
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String codedFileName = "Temporary_files";
		Workbook workbook = null;
		String[] exportFields = null;

		Object exportFieldStr =  model.get(NormalExcelConstants.EXPORT_FIELDS);
		if(exportFieldStr!=null && exportFieldStr!=""){
			exportFields = exportFieldStr.toString().split(",");
		}
		if (model.containsKey(NormalExcelConstants.MAP_LIST)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) model.get(NormalExcelConstants.MAP_LIST);
			if (list.size() == 0) {
				throw new RuntimeException("MAP_LIST IS NULL");
			}
			workbook = ExcelExportUtil.exportExcel((ExportParams) list.get(0).get(NormalExcelConstants.PARAMS), (Class<?>) list.get(0).get(NormalExcelConstants.CLASS), (Collection<?>) list.get(0).get(NormalExcelConstants.DATA_LIST),exportFields);
			for (int i = 1; i < list.size(); i++) {
				new ExcelExportServer().createSheet(workbook, (ExportParams) list.get(i).get(NormalExcelConstants.PARAMS), (Class<?>) list.get(i).get(NormalExcelConstants.CLASS), (Collection<?>) list.get(i).get(NormalExcelConstants.DATA_LIST),exportFields);
			}
		} else {
			workbook = ExcelExportUtil.exportExcel((ExportParams) model.get(NormalExcelConstants.PARAMS), (Class<?>) model.get(NormalExcelConstants.CLASS), (Collection<?>) model.get(NormalExcelConstants.DATA_LIST),exportFields);
		}
		if (model.containsKey(NormalExcelConstants.FILE_NAME)) {
			codedFileName = (String) model.get(NormalExcelConstants.FILE_NAME);
		}
		if (workbook instanceof HSSFWorkbook) {
			codedFileName += HSSF;
		} else {
			codedFileName += XSSF;
		}
		if (isIE(request)) {
			codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF8");
		} else {
			codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}
}
