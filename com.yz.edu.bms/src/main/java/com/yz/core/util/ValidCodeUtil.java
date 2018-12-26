package com.yz.core.util;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.predefined.WobbleRippleFilterFactory;
import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import com.yz.core.security.manager.SessionUtil;

public class ValidCodeUtil {

	private static final Logger log = LoggerFactory.getLogger(ValidCodeUtil.class);

	private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
	private static Random random = new Random();
	static {
		// cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));

		cs.setColorFactory(new ColorFactory() {
			@Override
			public Color getColor(int x) {
				int[] c = new int[3];
				int i = random.nextInt(c.length);
				for (int fi = 0; fi < c.length; fi++) {
					if (fi == i) {
						c[fi] = random.nextInt(71);
					} else {
						c[fi] = random.nextInt(256);
					}
				}
				return new Color(c[0], c[1], c[2]);
			}
		});
		RandomWordFactory wf = new RandomWordFactory();
		wf.setCharacters("23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ");
		wf.setMaxLength(4);
		wf.setMinLength(4);
		cs.setWordFactory(wf);
	}

	public static String createValidCode(HttpServletResponse response, int validCodeTimeout)
			throws ServletException, IOException {

		/*switch (random.nextInt(5)) {
		case 0:
			cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
			break;
		case 1:
			cs.setFilterFactory(new MarbleRippleFilterFactory());
			break;
		case 2:
			cs.setFilterFactory(new DoubleRippleFilterFactory());
			break;
		case 3:
			cs.setFilterFactory(new WobbleRippleFilterFactory());
			break;
		case 4:
			cs.setFilterFactory(new DiffuseRippleFilterFactory());
			break;
		}*/
		
		cs.setFilterFactory(new WobbleRippleFilterFactory());
		
		setResponseHeaders(response);
		
		// = EncoderHelper.getChallangeAndWriteImage(cs, "png",
		// response.getOutputStream());
		Captcha captcha = cs.getCaptcha();
		String token = captcha.getChallenge();
		SessionUtil.setValidCode(token);// 存入验证码
		ServletOutputStream os = response.getOutputStream();
		ImageIO.write(captcha.getImage(), "png", os);
		os.flush();
		os.close();
		return token;

	}

	private static void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}

}
