package main.java;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by Olaf on 2017-10-17.
 */
public class ClassLoaderGUI extends JFrame {

    private String componentsDir = "compiled";

    private JComboBox<String> files = new JComboBox<>();
    private JFileChooser fileChooser = new JFileChooser();

    public ClassLoaderGUI()  {
        super("Custom class loader");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(500, 300);
        setLocation(400, 400);


        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);
        this.componentsDir = fileChooser.getSelectedFile().getAbsolutePath();
        setComboBoxValues();
        add(files);
    }

    private void setComboBoxValues(){
        File directory = new File(this.componentsDir);
        System.out.println(directory.getName());
        File[] classesFiles = directory.listFiles();
        for (File file: classesFiles) {
            System.out.println(file.getName());
            this.files.addItem(file.getName());
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClassLoaderGUI().setVisible(true);
            }
        });
    }
}
