package com.yz.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel工具类， 只支持单sheet导出
 * 
 * @author Administrator
 *
 */
public class ExcelUtil {
	/**
	 * 导出
	 * 
	 * @param dest
	 *            导出结果集
	 * @param config
	 *            配置信息
	 * @return poi excel对象
	 */
	public static SXSSFWorkbook exportWorkbook(List dest, IExcelConfig config) {
		Assert.notNull(dest, "导出结果不能为空");
		Assert.notNull(config, "excel配置信息不能为空");

		Class clz = config.getType();
		LinkedList<IExcelTitle> titles = config.getTitles();
		String sheetName = config.getSheetName();

		SXSSFWorkbook  wb = new SXSSFWorkbook(100);

		CellStyle titleStyle = wb.createCellStyle();

		CellStyle paramStyle = wb.createCellStyle();
		
		Font font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);

		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setFont(font);
		paramStyle.setAlignment(HorizontalAlignment.CENTER);
		paramStyle.setWrapText(true);
		paramStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);  

		SXSSFSheet sheet = null;
		if (!StringUtil.hasValue(sheetName)) {
			sheet = wb.createSheet(sheetName);
		} else {
			sheet = wb.createSheet(DateUtil.getCurrentDate());
		}

		int size = dest.size() + 1;

		for (int i = 0; i < size; i++) {
			if (i == size)
				break;
			Object t = null;
			if (i > 0) {
				t = dest.get(i - 1);
			}
			SXSSFRow row = sheet.createRow(i);

			for (int j = 0; j < titles.size(); j++) {
				IExcelTitle title = titles.get(j);
				SXSSFCell cell = row.createCell(j);
				int width = 0;
				if (i == 0) {
					cell.setCellStyle(titleStyle);
					cell.setCellValue(title.getTitleName());
					if (title.getTitleName().getBytes().length > width) {
						width = title.getTitleName().getBytes().length;
					}
				} else {

					Object value = null;
					if (clz.isAssignableFrom(Map.class)) {
						value = ((Map) t).get(title.getParamName());
					} else {
						try {
							Method getterMethod = MethodUtil.getterMethod(clz, title.getParamName());
							if (getterMethod == null)
								continue;
							value = getterMethod.invoke(t, null);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}

					cell.setCellStyle(paramStyle);
					if (value != null) {
						int n = value.toString().getBytes().length;
						if (n > width) {
							width = n;
						}
						cell.setCellValue(value.toString());
					}
				}

				if (width * 2 > 255) {
					sheet.setColumnWidth(j, 255 * 256);
				} else if (width <= 0) {
					sheet.setColumnWidth(j, 10 * 2 * 256);
				} else {
					sheet.setColumnWidth(j, width * 2 * 256);
				}
			}
		}

		return wb;
	}

	/**
	 * 导入
	 * 
	 * @param is
	 *            文件流
	 * @param config
	 *            配置信息
	 * @return 导入结果集
	 */
	/*
	 * public static <T> List<T> importWorkbook(InputStream is, IExcelConfig<T>
	 * config) { Workbook wb = null; LinkedList<IExcelTitle> titles =
	 * config.getTitles();
	 * 
	 * List<T> result = new ArrayList<T>();
	 * 
	 * String sheetName = config.getSheetName(); Class<T> clz =
	 * config.getType();
	 * 
	 * try { wb = new HSSFWorkbook(is); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * Sheet sheet = null;
	 * 
	 * if (!StringUtil.hasValue(sheetName)) { sheet = wb.getSheet(sheetName); }
	 * else { sheet = wb.getSheetAt(0); } int num = 0; Iterator<Row> ri =
	 * sheet.rowIterator(); while (ri.hasNext()) { Row row = ri.next(); if
	 * (num++ == 0) continue; Iterator<Cell> ci = row.cellIterator();
	 * 
	 * T instance = null; try { instance = clz.newInstance(); } catch
	 * (InstantiationException e1) { e1.printStackTrace(); } catch
	 * (IllegalAccessException e1) { e1.printStackTrace(); }
	 * 
	 * int i = 0; while (ci.hasNext()) { IExcelTitle title = titles.get(i++);
	 * String paramName = title.getParamName(); Cell cell = ci.next();
	 * cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	 * 
	 * String value = null; switch (cell.getCellTypeEnum()) { case BOOLEAN: //
	 * 得到Boolean对象的方法 value = cell.getBooleanCellValue() + ""; break; case
	 * NUMERIC: if (HSSFDateUtil.isCellDateFormatted(cell)) { value =
	 * cell.getDateCellValue() + ""; } else { value = cell.getNumericCellValue()
	 * + ""; } break; case FORMULA: value = cell.getCellFormula(); break; case
	 * STRING: value = cell.getRichStringCellValue().toString(); break; }
	 * 
	 * if (clz.isAssignableFrom(Map.class)) { ((Map)
	 * instance).put(title.getParamName(), value); } else { Method setterMethod
	 * = null; try { setterMethod = MethodUtil.setterMethod(clz, paramName,
	 * String.class); setterMethod.invoke(instance, value); } catch
	 * (NoSuchMethodException | SecurityException | IllegalAccessException |
	 * IllegalArgumentException | InvocationTargetException e) {
	 * e.printStackTrace(); } if (setterMethod == null) continue; }
	 * 
	 * }
	 * 
	 * result.add(instance); }
	 * 
	 * return result; }
	 */

	/**
	 * 导入
	 * 
	 * @param is
	 *            文件输入流
	 * @param config
	 *            配置
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static <T> List<T> importWorkbook(InputStream is, IExcelConfig<T> config, String fileName) {
		Workbook wb = null;
		LinkedList<IExcelTitle> titles = config.getTitles();

		List<T> result = new ArrayList<T>();

		String sheetName = config.getSheetName();
		Class<T> clz = config.getType();

		try {
			if (isExcel2003(fileName)) {
				wb = new HSSFWorkbook(is);
			} else if (isExcel2007(fileName)) {
				wb = new XSSFWorkbook(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = null;

		if (!StringUtil.hasValue(sheetName)) {
			sheet = wb.getSheet(sheetName);
		} else {
			sheet = wb.getSheetAt(0);
		}
		int num = 0;

		int rowEnd = sheet.getLastRowNum();

		for (int rowNum = 0; rowNum <= rowEnd; rowNum++) {

			Row row = sheet.getRow(rowNum);

			if (num++ == 0)
				continue;

			int lastColumn = row.getLastCellNum();

			T instance = null;
			try {
				instance = clz.newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

			int i = 0;
			for (int cNum = 0; cNum < lastColumn; cNum++) {
				IExcelTitle title = titles.get(i++);
				String paramName = title.getParamName();
				Cell cell = row.getCell(cNum);
				String value = null;
				if (null != cell) {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					switch (cell.getCellTypeEnum()) {
					case BOOLEAN:
						// 得到Boolean对象的方法
						value = cell.getBooleanCellValue() + "";
						break;
					case NUMERIC:
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							value = cell.getDateCellValue() + "";
						} else {
							value = cell.getNumericCellValue() + "";
						}
						break;
					case FORMULA:
						value = cell.getCellFormula();
						break;
					case STRING:
						value = cell.getRichStringCellValue().toString();
						break;
					}

					if (!title.allowNull) {
						Assert.hasText(value, title.getTitleName() + "不能为空，请检查第" + num + "行数据");
					}
				}

				if (clz.isAssignableFrom(Map.class)) {
					((Map) instance).put(title.getParamName(), value);
				} else {
					Method setterMethod = null;
					try {
						setterMethod = MethodUtil.setterMethod(clz, paramName, String.class);
						setterMethod.invoke(instance, value);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					if (setterMethod == null)
						continue;
				}

			}

			result.add(instance);
		}

		return result;
	}

	/**
	 * 
	 * @描述：是否是2003的excel，返回true是2003
	 * 
	 * @返回值：boolean
	 */
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 
	 * @描述：是否是2007的excel，返回true是2007
	 * 
	 * @返回值：boolean
	 */

	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 * excel配置信息
	 * 
	 * @author Administrator
	 *
	 * @param <E>
	 */
	public static class IExcelConfig<E> {
		private LinkedList<IExcelTitle> titles = new LinkedList<IExcelTitle>();

		private Class type;

		public IExcelConfig() {
		}

		public LinkedList<IExcelTitle> getTitles() {
			return titles;
		}

		/**
		 * 添加表头格式信息
		 * 
		 * @param title
		 * @return
		 */
		public IExcelConfig addTitle(IExcelTitle title) {
			titles.add(title);
			return this;
		}

		/**
		 * sheet名称
		 */
		private String sheetName;

		public Class<E> getType() {
			return type;
		}

		public IExcelConfig<E> setType(Class<E> type) {
			this.type = type;
			return this;
		}

		public String getSheetName() {
			return sheetName;
		}

		/**
		 * 设置sheet名称
		 * 
		 * @param sheetName
		 * @return
		 */
		public IExcelConfig setSheetName(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}
	}

	/**
	 * 每个cell的表头，以及结果集中的参数名
	 * 
	 * @author Administrator
	 *
	 */
	public static class IExcelTitle {
		/**
		 * 名称
		 */
		private String titleName;

		/**
		 * 参数名称
		 */
		private String paramName;

		/**
		 * 是否可以为空
		 */
		private boolean allowNull = true;

		public IExcelTitle(String titleName, String paramName) {
			this.titleName = titleName;
			this.paramName = paramName;
		}

		public IExcelTitle(String titleName, String paramName, boolean allowNull) {
			this.titleName = titleName;
			this.paramName = paramName;
			this.allowNull = allowNull;
		}

		public String getTitleName() {
			return titleName;
		}

		public void setTitleName(String titleName) {
			this.titleName = titleName;
		}

		public String getParamName() {
			return paramName;
		}

		public void setParamName(String paramName) {
			this.paramName = paramName;
		}

		public boolean isAllowNull() {
			return allowNull;
		}

		public void setAllowNull(boolean allowNull) {
			this.allowNull = allowNull;
		}

	}

}