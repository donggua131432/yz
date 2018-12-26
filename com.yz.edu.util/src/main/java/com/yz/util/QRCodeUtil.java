package com.yz.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.encoder.QRCode;

public class QRCodeUtil {
	/**
	 * 生成二维码
	 * 
	 * @throws WriterException
	 * @throws IOException
	 */
	public static BufferedImage createQrcodeToBufferedImage(String content) throws WriterException, IOException {
		String format = "png";// 图像类型
		// 生成二维码中要存储的信息
		// 设置一下二维码的像素
		int width = 67 + 12 * 6;
		int height = 67 + 12 * 6;
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
		BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);// 输出图像
		return image;
	}

	/**
	 * 生成二维码
	 * 
	 * @throws WriterException
	 * @throws IOException
	 */
	public static ByteArrayOutputStream createQrcodeToStream(String content) throws WriterException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String format = "png";// 图像类型
		// 生成二维码中要存储的信息
		// 设置一下二维码的像素
		int width = 67 + 12 * 6;
		int height = 67 + 12 * 6;
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
		MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);// 输出图像
		return outputStream;
	}
}
