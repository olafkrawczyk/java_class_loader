package main.java;

import java.io.*;

/**
 * Created by Olaf on 2017-10-17.
 */

public class CustomClassLoader extends ClassLoader {

    public static final String CLASS_EXTENSION = ".class";
    private String searchDirectoryName;


    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class loadClass(String className) throws ClassNotFoundException {

        if (className.startsWith("main.java.compiled") || classFileInDirectory(className)) {
            return getClass(className.replace("main.java.compiled.", ""));
        }
        return super.loadClass(className, true);
    }

    private boolean classFileInDirectory(String className) {
        File classesDir = new File(searchDirectoryName);
        File[] filesInDir = classesDir.listFiles();
        if (filesInDir != null) {
            for (File file: filesInDir) {
                if (file.getName().equals(className + CLASS_EXTENSION)){
                    return true;
                }
            }
        }
        return false;
    }

    private Class getClass(String className) throws ClassNotFoundException {
        byte[] b = null;
        try {
            b = loadClassFileData(searchDirectoryName + File.separator + className + CLASS_EXTENSION);
            Class c = defineClass("main.java.compiled." + className, b, 0, b.length);
            resolveClass(c);
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] loadClassFileData(String path) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        int size = stream.available();
        byte buff[] = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        in.readFully(buff);
        in.close();
        return buff;
    }

    public void setSearchDirectoryName(String searchDirectoryName) {
        this.searchDirectoryName = searchDirectoryName;
    }

}
