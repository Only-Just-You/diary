package org.example.diary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.Date;

public class DiaryInputInterface extends JFrame {
    private final JTextField titleTextField;
    private final JTextField dateTextField;
    private final JTextArea textArea;
    private final static String urlStr = "D:/Documents/cjgong/";
    private final static String todayDate = String.format("%tF", new Date());

    static {
        File file = new File(urlStr);
        if(!file.exists()) file.mkdirs();
    }

    public static void main(String[] args) {
        try {
            DiaryInputInterface frame = new DiaryInputInterface();
            frame.setLocation(200,200);
            frame.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public DiaryInputInterface(){
        super();
        setTitle("日记簿");
        setBounds(100,100,900,475);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        textArea = new JTextArea(20,80);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("宋体",Font.BOLD,16));

        JLabel softLable = new JLabel();
        softLable.setFont(new Font("",Font.BOLD,22));
        softLable.setHorizontalAlignment(SwingConstants.CENTER);
        softLable.setText("日 志 簿");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        JLabel titleLable = new JLabel();
        titleLable.setText("标 题：");

        titleTextField = new JTextField();
        titleTextField.setColumns(30);
        titleTextField.setText("请输入标题");
        titleTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                titleTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                String date = titleTextField.getText().trim();
                if(date.length() == 0) titleTextField.setText("请输入标题");
            }
        });

        JLabel dateLable = new JLabel();
        dateLable.setText("日 期：");
        dateTextField = new JTextField();
        dateTextField.setColumns(30);
        dateTextField.setText(todayDate);
        dateTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                dateTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                String date = dateTextField.getText().trim();
                if(date.length() != 10) dateTextField.setText(todayDate);
            }
        });

        JButton seeButton = new JButton("查看日志");
        seeButton.addActionListener(new SeeButtonActionListener());

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(20);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(flowLayout);

        JButton saveButton = new JButton();
        saveButton.setText("保存");
        saveButton.addActionListener(new SaveButtonActionListener());

        JButton clearButton = new JButton();
        clearButton.setText("清空");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titleTextField.setText("请输入标题");
                dateTextField.setText(todayDate);
                textArea.setText("");
            }
        });

        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        getContentPane().add(softLable,BorderLayout.NORTH);
        getContentPane().add(contentPanel,BorderLayout.CENTER);
        getContentPane().add(buttonPanel,BorderLayout.SOUTH);

        contentPanel.add(infoPanel,BorderLayout.CENTER);

        infoPanel.add(titleLable);
        infoPanel.add(titleTextField);
        infoPanel.add(dateLable);
        infoPanel.add(dateTextField);
        infoPanel.add(seeButton);
        infoPanel.add(textArea);

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
    }

    private class SeeButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            DiaryList listFrame = new DiaryList();
            listFrame.setVisible(true);
            File text = listFrame.getText();
            listFrame.dispose();
            if(text != null){
                String[] infos = text.getName().split("\\(|\\)");
                titleTextField.setText(infos[0]);
                dateTextField.setText(infos[1]);
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;
                try{
                    fileReader = new FileReader(text);
                    bufferedReader = new BufferedReader(fileReader);
                    String data;
                    while ((data = bufferedReader.readLine()) != null){
                        textArea.append(data + "\n");
                    }
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }finally {
                    try {
                        if (bufferedReader != null) bufferedReader.close();
                        if (fileReader != null) fileReader.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

    private class SaveButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleTextField.getText();
            String date = dateTextField.getText();
            String name = title + "(" + date + ").txt";
            File file = new File(urlStr + name);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;
            try {
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(textArea.getText());
                bufferedWriter.flush();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }finally {
                try {
                    if(bufferedWriter != null) bufferedWriter.close();
                    if(fileWriter != null) fileWriter.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
