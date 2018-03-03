/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vincentyu.imageshirinker;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author vincentyu
 */
public class ImageShrinker {

    public static void shrinkImgByRadio(File src, File des, double radio) throws IOException {
        BufferedImage bi = ImageIO.read(src);
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(radio, radio), null);
        BufferedImage newBi = op.filter(bi, null);
        ImageIO.write(newBi, Utils.getExtension(src), des);
    }

    public static void shrinkImgByMaxWidth(File src, File des, double maxWidth) throws IOException {
        BufferedImage bi = ImageIO.read(src);// 檔案太大會拋 javax.imageio.IIOException: Unsupported Image Type
        int h = bi.getHeight();
        int w = bi.getWidth();
        double ratio = 0.;
        if (h > maxWidth || w > maxWidth) {
            if (h > w) {
                ratio = maxWidth / h;
            } else {
                ratio = maxWidth / w;
            }
            ImageShrinker.shrinkImgByRadio(src, des, ratio);
        } else {
            ImageIO.write(bi, Utils.getExtension(src), des);
        }
    }

    public static void shrinkImgByDefault(File src, File des) throws IOException {
        shrinkImgByMaxWidth(src, des, 120.);
    }

    public static void shrinkImgsByRadio(File[] srcs, File dir, double radio) throws IOException {
        for (File src : srcs) {
            File des = new java.io.File(dir, src.getName());
            shrinkImgByRadio(src, des, radio);
        }
    }

    public static void shrinkImgsByMaxWidth(File[] srcs, File dir, double maxWidth) throws IOException {
        for (File src : srcs) {
            File des = new java.io.File(dir, src.getName());
            shrinkImgByMaxWidth(src, des, maxWidth);
        }
    }

    public static void shrinkImgsByDefault(File[] srcs, File dir) throws IOException {
        for (File src : srcs) {
            File des = new java.io.File(dir, src.getName());
            shrinkImgByDefault(src, des);
        }
    }
}
