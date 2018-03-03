/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vincentyu.imageshirinker;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author vincent
 */
public class Main extends JFrame {

    File[] srcs;
    File dir;
    double ratio;
    double maxWidth;
    JTextField jtfDir = new JTextField();
    JTextField jtfMax = new JTextField();
    JButton btnAdd = new JButton("加入圖片");
    JButton btnRemove = new JButton("移除圖片");
    JButton btnRemoveAll = new JButton("移除全部");
    JButton btnDir = new JButton("開啟資料夾");
    JButton btnExc = new JButton("執行");
    DefaultListModel listData = new DefaultListModel();
    JLabel jlPic = new JLabel();
    JList list = new JList(listData);
    JFileChooser imgFc = new JFileChooser();
    JFileChooser dirFc = new JFileChooser();

    Main() {
        initComponents();

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setCellRenderer(new ImageCellRenderer());

        imgFc.setMultiSelectionEnabled(true);
        //Add a custom file filter and disable the default
        //(Accept All) file filter.
        imgFc.addChoosableFileFilter(new ImageFilter());
        imgFc.setAcceptAllFileFilterUsed(false);

        //Add custom icons for file types.
        imgFc.setFileView(new ImageFileView());

        //Add the preview pane.
        imgFc.setAccessory(new ImagePreview(imgFc));

        dirFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addF();
            }
        });

        btnRemove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeF();
            }
        });
        btnRemoveAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeAllF();
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                showPic();
            }
        });

        btnDir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addDir();
            }
        });

        jtfMax.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                jtfMax.selectAll();
            }

            public void focusLost(FocusEvent e) {
            }
        });

        btnExc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doExc();
            }
        });

        settleJFrame();
    }

    void initComponents() {
        this.setTitle("圖片縮放至指定資料夾 - Vincent");
        //jpUp

        JPanel jpUp = new JPanel();
        jpUp.setLayout(new BorderLayout());

        jlPic.setHorizontalAlignment(JLabel.CENTER);
        jlPic.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel jpAdd = new JPanel();
        jpAdd.setLayout(new GridLayout(3, 1, 5, 5));
        jpAdd.add(btnAdd);
        jpAdd.add(btnRemove);
        jpAdd.add(btnRemoveAll);
        jpUp.add(jpAdd, BorderLayout.WEST);

        JScrollPane jScroll = new JScrollPane(list);
        list.setVisibleRowCount(7);
        jpUp.add(jScroll, BorderLayout.EAST);
        jpUp.setBorder(BorderFactory.createTitledBorder("取得所有要縮放的圖片"));
        jpUp.add(jlPic);
        this.add(jpUp, BorderLayout.NORTH);

        //jpMid

        JPanel jpMid = new JPanel();
        jpMid.setBorder(BorderFactory.createTitledBorder("圖片存放至指定資料夾"));
        jpMid.add(new JLabel("要存放圖片的資料夾路徑", JLabel.RIGHT));
        jtfDir.setColumns(30);
        jpMid.add(jtfDir);
        jpMid.add(btnDir);
        this.add(jpMid, BorderLayout.CENTER);

        //jpBottom
        JPanel jpBottom = new JPanel();
        jpBottom.add(new JLabel("設定圖片最大的寬與高 : ", JLabel.RIGHT));
        jtfMax.setText(String.valueOf(500));
        jtfMax.setHorizontalAlignment(JTextField.RIGHT);
        jtfMax.setColumns(5);
        jpBottom.add(jtfMax);
        jpBottom.add(new JLabel("(像素)", JLabel.LEFT));
        jpBottom.add(btnExc);
        this.add(jpBottom, BorderLayout.SOUTH);

    }

    void settleJFrame() {
        this.setSize(500, 400);
        double y = (this.getToolkit().getScreenSize().getHeight() - this.getHeight()) / 2;
        double x = (this.getToolkit().getScreenSize().getWidth() - this.getWidth()) / 2;
        this.setLocation((int) x, (int) y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setResizable(false);
        this.setVisible(true);
    }

    void addF() {
        int returnVal = imgFc.showDialog(Main.this, "選擇圖片");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            for (File f : imgFc.getSelectedFiles()) {
                if (!listData.contains(f)) {
                    listData.addElement(f);
                }
            }
        }
        list.setSelectedIndex(0);
    }

    void removeF() {
        int max = list.getMaxSelectionIndex();
        int min = list.getMinSelectionIndex();
        listData.removeRange(min, max);
        list.clearSelection();
        if (listData.capacity() > 0) {
            list.setSelectedIndex(0);
        }
    }

    void removeAllF() {
        listData.clear();
        list.clearSelection();
        System.gc();
        pack();
    }

    void showPic() {
        if (list.isSelectionEmpty()) {
            jlPic.setIcon(null);
            Main.this.pack();
            return;
        }
        File f = (File) list.getSelectedValue();
        ImageIcon icon = null;
        BufferedImage image = null;
        double radio = 0.;
        try {
            image = ImageIO.read(f);
            if (image.getHeight() > image.getWidth()) {
                radio = 300. / image.getHeight();
            } else {
                radio = 300. / image.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(radio, radio), null);
            image = op.filter(image, null);
        } catch (IOException ex) {
            Logger.getLogger(ImageCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        icon = new ImageIcon(image);
        jlPic.setIcon(icon);
        pack();
    }

    void addDir() {
        int returnVal = this.dirFc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dirF = dirFc.getSelectedFile();
            this.jtfDir.setText(dirF.getPath());
        }
    }

    void doExc() {
        try {
            maxWidth = Integer.parseInt(jtfMax.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "必須填入數值");
            return;
        }
        if (maxWidth > 3000 || maxWidth < 1) {
            JOptionPane.showMessageDialog(this, "數字不正確");
            return;
        }
        srcs = new File[listData.size()];
        for (int i = 0; i < srcs.length; ++i) {
            srcs[i] = (File) listData.get(i);
        }
        if (srcs == null) {
            JOptionPane.showMessageDialog(this, "必須選擇圖片");
            return;
        }
        dir = new File(jtfDir.getText());
        if (!dir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "請選擇一個資料夾");
            return;
        }
        this.setTitle("壓縮中請等一下...");
        try {
            ImageShrinker.shrinkImgsByMaxWidth(srcs, dir, maxWidth);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "錯誤: 寫入失敗!");
        }
        this.setTitle("圖片縮放至指定資料夾");

        //init
        this.removeAllF();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main();
    }
}
