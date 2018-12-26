package com.yz.core.util;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.conf.YzSysConfig;

 
/**
 * 条形码工具类
 *
 * @author tangzz
 * @createDate 2015年9月17日
 *
 */
@Component
public class BarcodeUtil {
	
	@Autowired
	private YzSysConfig yzSysConfig ; 
 
    /**
     * 生成文件
     *
     * @param msg
     * @param path
     * @return
     */
    public  File generateFile(String msg, String path) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
 
    /**
     * 生成字节
     *
     * @param msg
     * @return
     */
    public  byte[] generate(String msg) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg, ous);
        return ous.toByteArray();
    }
 
    /**
     * 生成字节,并上传至OSS文件服务器
     *
     * @param msg
     * @return
     */
    public  void uploadBarcodeImg(String barCode) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(barCode, ous); 
        String barCode_packge=barCode+"-1-1-";
        ByteArrayOutputStream ous_package = new ByteArrayOutputStream();
        generate(barCode_packge, ous_package);
        String bucket = yzSysConfig.getBucket();
        
		String fileName = FileSrcUtil.Type.SFEXPRESS.get() + "/" + barCode + ".png";
		String fileName_package = FileSrcUtil.Type.SFEXPRESS.get() + "/" + barCode+"-1-1-" + ".png";
		FileUploadUtil.upload(bucket, fileName, ous.toByteArray());
		FileUploadUtil.upload(bucket, fileName_package, ous_package.toByteArray());
    }
    
    /**
     * 生成到流
     *
     * @param msg
     * @param ous
     */
    public static void generate(String msg, OutputStream ous) {
        if (StringUtils.isEmpty(msg) || ous == null) {
            return;
        }
 
        //Code39Bean bean = new Code39Bean();
        Code128Bean bean = new Code128Bean();
        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(2.0f / dpi); 
        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.doQuietZone(false);
        String format = "image/png";
        try {
 
            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);
 
            // 生成条形码
            bean.generateBarcode(canvas, msg);
 
            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
 
    public static void main(String[] args) {
        String msg = "VC41557268955";
        String path = "barcode.png";
       // generateFile(msg, path);
        
        //uploadBarcodeImg(msg);
        System.out.println("成功");
    }
}
