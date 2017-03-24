package com.compus.second.Utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.compus.second.Exception.InvalidException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Random;

/**
 * Created by cai on 2017/2/20.
 */
public class ImageService {


    public static String  uploadImageToImageServer(CommonsMultipartFile image,String key) throws IOException {

        InputStream inputStream = image.getInputStream();
        String realName = image.getOriginalFilename();
        int extpoint = realName.indexOf(".");
        String ext =  realName.substring(extpoint,realName.length());


        // 判断上传的文件是不是一个图片(png,jpeg)
        if (!ext.toUpperCase().equals(".PNG") && !ext.toUpperCase().equals(".JPG"))
            throw new InvalidException(403,"无效的文件,请选择png文件或者jpg文件");

        String endpoint;
        if (SystemUtils.IS_OS_LINUX) {
            endpoint = "oss-cn-shanghai-internal.aliyuncs.com";
        }
        else {
            endpoint = "oss-cn-shanghai.aliyuncs.com";
        }
        String imageName = key +Utils.parseDateToString(new Date(),"yyyyMMddHHmmssSSS");
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public,max-age=" + 60*60*24*30);
        if (ext.equals("jpg"))
            meta.setContentType("image/jpeg");
        else{ meta.setContentType("image/png");}
        meta.setLastModified(new Date());
        String ossKey = "secondhand/" + imageName +ext;

        OSSClient client = null;
        try {

            client = new OSSClient(endpoint, "YUWa6JyS0EwLZfP1", "V6T1YtMJDFoJzB6OrtCYHUJkU558CG");
            boolean tf = client.doesObjectExist("fudatour",imageName);

            if (tf == true)
            {
                client.deleteObject("fudatour",imageName);
            }

            client.putObject("fudatour", ossKey, inputStream, meta);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return imageName;
    }

    // 验证码字符集
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    // 字符数量
    private static final int SIZE = 4;
    // 干扰线数量
    private static final int LINES = 5;
    // 宽度
    private static final int WIDTH = 80;
    // 高度
    private static final int HEIGHT = 40;
    // 字体大小
    private static final int FONT_SIZE = 30;

    /**
     * 生成随机验证码及图片
     * Object[0]：验证码字符串；
     * Object[1]：验证码图片。
     */
    public static Object[] createImage() {
        StringBuffer sb = new StringBuffer();
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        // 5.画随机字符
        Random ran = new Random();
        for (int i = 0; i <SIZE; i++) {
            // 取随机字符索引
            int n = ran.nextInt(chars.length);
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 设置字体大小
            graphic.setFont(new Font(
                    null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            // 画字符
            graphic.drawString(
                    chars[n] + "", i * WIDTH / SIZE, HEIGHT*2/3);
            // 记录字符
            sb.append(chars[n]);
        }
        // 6.画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT),
                    ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }
        // 7.返回验证码和图片
        return new Object[]{sb.toString(), image};
    }

    /**
     * 随机取色
     */
    public static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));
        return color;
    }
}
