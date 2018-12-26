package com.yz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Hello world!
 *
 */
public class ExcelUtilExample {

	public static ExcelUtil.IExcelConfig<Test> testExcelCofing = new ExcelUtil.IExcelConfig<Test>();

	static {
		testExcelCofing.setType(Test.class)
				.addTitle(new ExcelUtil.IExcelTitle("编码", "code"))
				.addTitle(new ExcelUtil.IExcelTitle("名称", "name"));
	}

	public static void main(String[] args) throws IOException {
		write(); //导出
		//		read();//导入
	}

	public static void read() throws IOException {
		String filePath = "A:\\test.xls";

		FileInputStream fis = new FileInputStream(filePath);

		List<Test> list = ExcelUtil.importWorkbook(fis, testExcelCofing,filePath);

		for (Test t : list) {
			System.out.println(t.getCode() + " | " + t.getName());
		}
	}

	public static void write() throws IOException {
		List<Test> dest = new ArrayList<Test>();
		for (int i = 0; i < 10; i++) {
			Test t = new Test();
			t.setCode(i + "xx");
			t.setName(i * 5 + "x" + i * 6);
			dest.add(t);
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(dest, testExcelCofing);

		String filePath = "A:\\test.xls";

		File f = new File(filePath);
		if (!f.exists()) {
			f.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(f);

		wb.write(fos);

		fos.flush();
		fos.close();
	}

	public static class Test {

		private String code;

		private String name;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
