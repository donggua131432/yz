package com.yz.core.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.yz.conf.YzSysConfig;

@Component
public class SfCode128CUtil {
	
	@Autowired
	private YzSysConfig yzSysConfig ; 
	
	static String[][] code128 = { { " ", " ", "00", "212222", "11011001100" },
			{ "!", "!", "01", "222122", "11001101100" }, { "\"", "\"", "02", "222221", "11001100110" },
			{ "#", "#", "03", "121223", "10010011000" }, { "$", "$", "04", "121322", "10010001100" },
			{ "%", "%", "05", "131222", "10001001100" }, { "&", "&", "06", "122213", "10011001000" },
			{ "'", "'", "07", "122312", "10011000100", }, { "(", "(", "08", "132212", "10001100100" },
			{ ")", ")", "09", "221213", "11001001000" }, { "*", "*", "10", "221312", "11001000100" },
			{ "+", "+", "11", "231212", "11000100100" }, { ",", ",", "12", "112232", "10110011100" },
			{ "-", "-", "13", "122132", "10011011100" }, { ".", ".", "14", "122231", "10011001110" },
			{ "/", "/", "15", "113222", "10111001100" }, { "0", "0", "16", "123122", "10011101100" },
			{ "1", "1", "17", "123221", "10011100110" }, { "2", "2", "18", "223211", "11001110010", },
			{ "3", "3", "19", "221132", "11001011100" }, { "4", "4", "20", "221231", "11001001110", },
			{ "5", "5", "21", "213212", "11011100100" }, { "6", "6", "22", "223112", "11001110100" },
			{ "7", "7", "23", "312131", "11101101110" }, { "8", "8", "24", "311222", "11101001100" },
			{ "9", "9", "25", "321122", "11100101100" }, { ":", ":", "26", "321221", "11100100110" },
			{ ";", ";", "27", "312212", "11101100100" }, { "<", "<", "28", "322112", "11100110100" },
			{ "=", "=", "29", "322211", "11100110010" }, { ">", ">", "30", "212123", "11011011000" },
			{ "?", "?", "31", "212321", "11011000110" }, { "@", "@", "32", "232121", "11000110110" },
			{ "A", "A", "33", "111323", "10100011000" }, { "B", "B", "34", "131123", "10001011000" },
			{ "C", "C", "35", "131321", "10001000110" }, { "D", "D", "36", "112313", "10110001000" },
			{ "E", "E", "37", "132113", "10001101000" }, { "F", "F", "38", "132311", "10001100010" },
			{ "G", "G", "39", "211313", "11010001000" }, { "H", "H", "40", "231113", "11000101000" },
			{ "I", "I", "41", "231311", "11000100010" }, { "J", "J", "42", "112133", "10110111000" },
			{ "K", "K", "43", "112331", "10110001110" }, { "L", "L", "44", "132131", "10001101110" },
			{ "M", "M", "45", "113123", "10111011000" }, { "N", "N", "46", "113321", "10111000110" },
			{ "O", "O", "47", "133121", "10001110110" }, { "P", "P", "48", "313121", "11101110110" },
			{ "Q", "Q", "49", "211331", "11010001110" }, { "R", "R", "50", "231131", "11000101110" },
			{ "S", "S", "51", "213113", "11011101000" }, { "T", "T", "52", "213311", "11011100010" },
			{ "U", "U", "53", "213131", "11011101110" }, { "V", "V", "54", "311123", "11101011000" },
			{ "W", "W", "55", "311321", "11101000110" }, { "X", "X", "56", "331121", "11100010110" },
			{ "Y", "Y", "57", "312113", "11101101000" }, { "Z", "Z", "58", "312311", "11101100010" },
			{ "[", "[", "59", "332111", "11100011010" }, { "\\", "\\", "60", "314111", "11101111010" },
			{ "]", "]", "61", "221411", "11001000010" }, { "^", "^", "62", "431111", "11110001010" },
			{ "_", "_", "63", "111224", "10100110000" }, { "NUL", "`", "64", "111422", "10100001100" },
			{ "SOH", "a", "65", "121124", "10010110000" }, { "STX", "b", "66", "121421", "10010000110" },
			{ "ETX", "c", "67", "141122", "10000101100" }, { "EOT", "d", "68", "141221", "10000100110" },
			{ "ENQ", "e", "69", "112214", "10110010000" }, { "ACK", "f", "70", "112412", "10110000100" },
			{ "BEL", "g", "71", "122114", "10011010000" }, { "BS", "h", "72", "122411", "10011000010" },
			{ "HT", "i", "73", "142112", "10000110100" }, { "LF", "j", "74", "142211", "10000110010" },
			{ "VT", "k", "75", "241211", "11000010010" }, { "FF", "I", "76", "221114", "11001010000" },
			{ "CR", "m", "77", "413111", "11110111010" }, { "SO", "n", "78", "241112", "11000010100" },
			{ "SI", "o", "79", "134111", "10001111010" }, { "DLE", "p", "80", "111242", "10100111100" },
			{ "DC1", "q", "81", "121142", "10010111100" }, { "DC2", "r", "82", "121241", "10010011110" },
			{ "DC3", "s", "83", "114212", "10111100100" }, { "DC4", "t", "84", "124112", "10011110100" },
			{ "NAK", "u", "85", "124211", "10011110010" }, { "SYN", "v", "86", "411212", "11110100100" },
			{ "ETB", "w", "87", "421112", "11110010100" }, { "CAN", "x", "88", "421211", "11110010010" },
			{ "EM", "y", "89", "212141", "11011011110" }, { "SUB", "z", "90", "214121", "11011110110" },
			{ "ESC", "{", "91", "412121", "11110110110" }, { "FS", "|", "92", "111143", "10101111000" },
			{ "GS", "},", "93", "111341", "10100011110" }, { "RS", "~", "94", "131141", "10001011110" },
			{ "US", "DEL", "95", "114113", "10111101000" }, { "FNC3", "FNC3", "96", "114311", "10111100010" },
			{ "FNC2", "FNC2", "97", "411113", "11110101000" }, { "SHIFT", "SHIFT", "98", "411311", "11110100010" },
			{ "CODEC", "CODEC", "99", "113141", "10111011110" }, { "CODEB", "FNC4", "CODEB", "114131", "10111101110" },
			{ "FNC4", "CODEA", "CODEA", "311141", "11101011110" }, { "FNC1", "FNC1", "FNC1", "411131", "11110101110" },
			{ "StartA", "StartA", "StartA", "211412", "11010000100" },
			{ "StartB", "StartB", "StartB", "211214", "11010010000" },
			{ "StartC", "StartC", "StartC", "211232", "11010011100" },
			{ "Stop", "Stop", "Stop", "2331112", "1100011101011" }, };

	private static int m_nImageHeight = 20; // 条码的高度像素数

	/**
	 * 生产Code128的条形码的code
	 * 
	 * @param barCode
	 * @param encode
	 * @return
	 */
	public  String getCode(String barCode, String encode) {
		String _Text = "";// 返回的参数
		List<Integer> _TextNumb = new ArrayList<Integer>();// 2截取位的组合
		int _Examine = 105; // 首位
		// 编码不能是奇数
		if (!((barCode.length() & 1) == 0))
			return "";
		while (barCode.length() != 0) {
			int _Temp = 0;
			try {
				// Code128 编码必须为数字
				_Temp = Integer.valueOf(barCode.substring(0, 2));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
			// 获得条纹
			_Text += getValue(barCode, barCode.substring(0, 2), _Temp);
			_TextNumb.add(_Temp);
			// 条码截取2个就需要去掉用过的前二位
			barCode = barCode.substring(2);
		}
		if (_TextNumb.size() == 0) {
			return "";
		}
		_Text = getValue(_Examine) + _Text; // 获取开始位
		for (int i = 0; i != _TextNumb.size(); i++) {
			_Examine += _TextNumb.get(i) * (i + 1);
		}
		_Examine = _Examine % 103; // 获得校验位
		_Text += getValue(_Examine); // 获取校验位
		_Text += "1100011101011"; // 结束位
		return _Text;
	}

	/**
	 * 根据编号获得条纹
	 * 
	 * @param encode
	 * @param p_Value
	 * @param p_SetID
	 * @return
	 */
	private  String getValue(String encode, String p_Value, int p_SetID) {
		return code128[p_SetID][4];
	}

	// / <summary>
	// / 根据编号获得条纹
	// / </summary>
	// / <param name="p_CodeId"></param>
	// / <returns></returns>
	private  String getValue(int p_CodeId) {
		return code128[p_CodeId][4];
	}

	// / <summary>
	// / 生成条码
	// / </summary>
	// / <param name="BarString">条码模式字符串</param>
	// / <param name="Height">生成的条码高度</param>
	// / <returns>条码图形</returns>
	public  void kiCode128C(String barCode, HttpServletResponse response) throws IOException {
		ServletOutputStream os = null;
		try {

			String barString = getCode(barCode.replaceAll(" ", ""), "");
			// setResponseHeaders(response);
			os = response.getOutputStream();
			if (null == barString || null == os || 0 == barString.length())
				return;
			int nImageWidth = 0;
			char[] cs = barString.toCharArray();
			for (int i = 0; i != cs.length; i++) {
				nImageWidth = cs.length;
			}

			BufferedImage bi = new BufferedImage(nImageWidth, m_nImageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();

			for (int i = 0; i < cs.length; i++) {
				if ("1".equals(cs[i] + "")) {
					g.setColor(Color.BLACK);
					g.fillRect(i, 0, 1, m_nImageHeight);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(i, 0, 1, m_nImageHeight);
				}
			}
			ImageIO.write(bi, "png", os);
		} catch (IOException e) {
			throw e;
		} finally {
			os.flush();
			os.close();
		}

	}

	/**
	 * 顺丰单号生成条形码，并上传至OSS文件服务器
	 * 
	 * @param barCode
	 * @throws IOException
	 */
	public  void uploadBarcodeImg(String barCode) throws IOException {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			barCode = barCode.replaceAll(" ", "");
			String barString = getCode(barCode, "");
			// setResponseHeaders(response);
			if (null == barString || null == out || 0 == barString.length())
				return;
			int nImageWidth = 0;
			char[] cs = barString.toCharArray();
			for (int i = 0; i != cs.length; i++) {
				nImageWidth = cs.length;
			}

			BufferedImage bi = new BufferedImage(nImageWidth, m_nImageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();

			for (int i = 0; i < cs.length; i++) {
				if ("1".equals(cs[i] + "")) {
					g.setColor(Color.BLACK);
					g.fillRect(i, 0, 1, m_nImageHeight);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(i, 0, 1, m_nImageHeight);
				}
			}
			ImageIO.write(bi, "png", out);
			String bucket = yzSysConfig.getBucket();
			String fileName = FileSrcUtil.Type.SFEXPRESS.get() + "/" + barCode + ".png";

			FileUploadUtil.upload(bucket, fileName, out.toByteArray());
		} catch (IOException e) {
			throw e;
		} finally {
			out.flush();
			out.close();
		}
	}

	protected  void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}

	public static void main(String[] args) {
		// endpoint以杭州为例，其它region请按实际情况填写
		String endpoint = "http://yzimstest.oss-cn-shenzhen.aliyuncs.com";
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录
		// https://ram.console.aliyun.com 创建
		String accessKeyId = "LTAICtJ0nsZL33rZ";
		String accessKeySecret = "iWfK0ZvQMEZQlddj23lF1iwz9O1eLH";
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		boolean found = ossClient.doesObjectExist("test", "barcode.png");
		System.err.println(found);

		// 使用访问OSS
		// 关闭ossClient
		ossClient.shutdown();
	}

}
