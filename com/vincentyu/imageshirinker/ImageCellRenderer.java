/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vincentyu.imageshirinker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author vincentyu
 */
public class ImageCellRenderer extends DefaultListCellRenderer {

    public ImageCellRenderer() {
        setBorder(new EmptyBorder(1, 1, 1, 1));
        //設定標籤使用空白框線
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        //呼叫基礎類別的getListCellRendererComponent()方法

        File thisF = (File) value;

        String name = thisF.getName();
        //取得選項物件並將型別轉換為File物件, 再取得檔案名稱

        setText(name); //設定標籤顯示的字串

        ImageIcon icon = null;
        BufferedImage image = null;
        double radio = 0.;
//        try {
//            image = ImageIO.read(thisF);
//            if (image.getHeight() > image.getWidth()) {
//                radio = 50. / image.getHeight();
//            } else {
//                radio = 50. / image.getWidth();
//            }
//            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(radio, radio), null);
//            image = op.filter(image, null);
//
//        } catch (IOException ex) {
//            Logger.getLogger(ImageCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        icon = new ImageIcon(image);
//        setIcon(icon);
        //判斷File物件是否代表資料夾, 以決定使用的圖示

        setBackground(isSelected
                ? list.getSelectionBackground() : list.getBackground());
        //設定選項標籤的背景顏色

        setForeground(isSelected
                ? list.getSelectionForeground() : list.getForeground());
        //設定選項標籤的前景顏色

        setBorder(cellHasFocus
                ? new CompoundBorder(
                new LineBorder(Color.darkGray, 1),
                new EmptyBorder(1, 1, 1, 1)) //合成框線
                : new EmptyBorder(1, 1, 1, 1)); //空白框線
        //設定標籤使用的框線

        return this;
    }
}
