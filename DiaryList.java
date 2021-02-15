package org.example.diary;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DiaryList extends JDialog {
    private static final File file;
    private final File[] files;
    private File text = null;
    private final JPanel allPanel;
    static {
        file = new File("D:/Documents/cjgong/");
    }

    public DiaryList(){
        super();
        setModal(true);
        setTitle("日记列表");
        setBounds(100,100,580,375);

        files = file.listFiles();
        allPanel = new JPanel();
        if (files != null) {
            allPanel.setPreferredSize(new Dimension(450,files.length * 36));
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(allPanel);

        if (files != null) {
            for(int i=0; i < files.length; i++){
                StringBuilder name = new StringBuilder(" " + files[i].getName());
                name = new StringBuilder(name.substring(0, name.length() - 4));
                while (name.length() != 30){
                    name.append(" ");
                }

                JPanel onePanel = new JPanel();
                onePanel.setBackground(Color.white);
                onePanel.setBorder(new LineBorder(Color.black,1,false));
                onePanel.setLayout(new BorderLayout());

                JLabel label = new JLabel();
                label.setFont(new Font("宋体",Font.BOLD,16));
                label.setText(name.toString());

                JButton seeButton = new JButton();
                seeButton.setText("查 看");
                seeButton.setName("" + i);
                seeButton.addActionListener(new SeeButtonActionListener());

                allPanel.add(onePanel);
                onePanel.add(label,BorderLayout.WEST);
                onePanel.add(seeButton,BorderLayout.EAST);
            }
        }

        JButton returnButton = new JButton();
        returnButton.setText("返 回");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JButton delAllButton = new JButton("一键清空");
        if (files != null) {
            delAllButton.setEnabled(files.length != 0);
        }
        delAllButton.addActionListener(new DelButtonActionListener());

        getContentPane().add(scrollPane);

        getContentPane().add(delAllButton,BorderLayout.EAST);
        getContentPane().add(returnButton,BorderLayout.SOUTH);
    }

    private class DelButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(files != null) {
                for (int i = files.length - 1; i >= 0; i--) {
                    files[i].delete();
                    allPanel.remove(i);
                }
            }
            SwingUtilities.updateComponentTreeUI(allPanel);
            JButton button = (JButton) e.getSource();
            button.setEnabled(false);
        }
    }

    private class SeeButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String name = button.getName();
            if (files != null) {
                text = files[Integer.parseInt(name)];
            }
            setVisible(false);
        }
    }

    public File getText(){
        return text;
    }
}
