package org.example.util;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CaptchaRecognizer {
    public static void main(String[] args) {
        ITesseract instance = new Tesseract();
        instance.setDatapath("C:\\Users\\asus\\Desktop\\test\\tessdata-4.1.0");
        instance.setLanguage("eng");
        try {
            File captchaImageFile = new File("C:\\Users\\asus\\Desktop\\test\\1234.png");
            BufferedImage captchaImage = ImageIO.read(captchaImageFile);
            String captchaCode = instance.doOCR(captchaImage);
            System.out.println("识别结果: " + captchaCode);
        } catch (TesseractException | IOException e) {
            e.printStackTrace();
        }
    }
}
