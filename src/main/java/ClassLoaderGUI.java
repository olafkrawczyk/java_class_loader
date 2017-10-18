package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;

/**
 * Created by Olaf on 2017-10-17.
 */
public class ClassLoaderGUI extends JFrame {

    private String componentsDir = "compiled";

    private JComboBox<String> files = new JComboBox<>();
    private JFileChooser fileChooser = new JFileChooser();
    private JButton openDirectory = new JButton("Open directory");
    private JButton loadClass = new JButton("Load selected class");

    public ClassLoaderGUI() {
        super("Custom class loader");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(500, 100);
        setLocation(400, 400);


        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        openDirectory.addActionListener(e -> {
            fileChooser.showOpenDialog(this);
            try {
                this.componentsDir = fileChooser.getSelectedFile().getCanonicalPath();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            setComboBoxValues();
        });

        loadClass.addActionListener(e -> {
            CustomClassLoader cCLoader = new CustomClassLoader(this.getClass().getClassLoader());
            cCLoader.setSearchDirectoryName(componentsDir);
            JFrame classView = new JFrame((String) files.getSelectedItem());
            classView.setSize(500, 200);
            classView.setLocation(500,500);
            classView.setLayout(new FlowLayout());
            classView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    System.gc();
                }
            });

            DefaultListModel<String> methodsListModel = new DefaultListModel<>();

            Method[] methods = {};
            Class loadedClass = null;
            try {
                loadedClass = cCLoader.loadClass((String) files.getSelectedItem());
                methods = loadedClass.getDeclaredMethods();
                for (Method method : methods) {
                    methodsListModel.addElement(method.getName());
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

            JList<String> methodsList = new JList<>(methodsListModel);
            Method[] finalMethods = methods;
            JButton invokeButton = new JButton("Invoke");
            Class finalLoadedClass = loadedClass;
            invokeButton.addActionListener(e1 -> {
                int index = methodsList.getSelectedIndex();
                Method selectedMethod = finalMethods[index];
                Parameter[] parameters = selectedMethod.getParameters();
                List<Object> arguments = new LinkedList<>();
                for (Parameter parameter : parameters) {
                    String value = JOptionPane.showInputDialog(parameter.getType().getSimpleName() + " " +parameter.getName());
                    if (parameter.getType() == int.class) {
                        arguments.add(Integer.valueOf(value));
                    }
                    else if (parameter.getType() == String.class) {
                        arguments.add(value);
                    }
                    else if (parameter.getType().getSimpleName().equals("String[]")) {
                        arguments.add(value.split(","));
                    }
                    else if (parameter.getType().getSimpleName().equals("int[]")) {
                        String[] values = value.split(",");
                        int[] argumentsArray = (int[]) Array.newInstance(int.class, values.length);
                        for (int k = 0; k < values.length; k++){
                            argumentsArray[k] = Integer.valueOf(values[k]);
                        }
                        arguments.add(argumentsArray);
                    }

                }
                if (Modifier.isStatic(selectedMethod.getModifiers())){
                    try {
                        JOptionPane.showConfirmDialog(null, (String) selectedMethod.invoke(null, arguments.toArray()));
                    } catch (IllegalAccessException | InvocationTargetException e2) {
                        e2.printStackTrace();
                    }
                }
                else {
                    try {
                        Object o = finalLoadedClass.newInstance();
                        if(selectedMethod.getReturnType().getName().startsWith("[")){
                            String resultString = "";
                            int[] result = (int[]) selectedMethod.invoke(o, arguments.toArray());
                            for(int res: result) {
                                resultString = resultString + res + " ";
                            }
                            JOptionPane.showConfirmDialog(null, resultString);
                        }
                        else {
                            JOptionPane.showConfirmDialog(null, selectedMethod.invoke(o, arguments.toArray()));
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e2) {
                        e2.printStackTrace();
                    }
                }

            });
            classView.add(methodsList);
            classView.add(invokeButton);

            classView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            classView.setVisible(true);
        });

        add(openDirectory);
        add(files);
        add(loadClass);
    }

    private void setComboBoxValues() {
        this.files.removeAllItems();
        File directory = new File(this.componentsDir);
        File[] classesFiles = directory.listFiles();
        for (File file : classesFiles) {
            if (file.getName().endsWith("class")) {
                this.files.addItem(file.getName().replace(".class", ""));
            }
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
