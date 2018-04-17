package com.faker.netty.core.common;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by faker on 18/4/11.
 */
public class AnnotationScanner {

    public static final List<String> clzCompleteNameList = new ArrayList<String>();
    public static final String basePath = AnnotationScanner.class.getResource("/").getPath();
    private static boolean initFlag = false;

    public static List<Class> listClzByAnnotation(Class clz) {
        List<Class> classList = new ArrayList<Class>();
        if (!initFlag) {
            searchFiles(new File(basePath));
        }

        try {
            for (String className : clzCompleteNameList) {
                Class templateClass = Class.forName(className);
                Annotation controller = templateClass.getAnnotation(clz);
                if (controller != null) {
                    classList.add(templateClass);
                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound");
            e.printStackTrace();
        }

        return classList;
    }

    private static void searchFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                AnnotationScanner.searchFiles(file1);
            }
        } else {
            String fileName = file.getAbsolutePath();
            if (!fileName.contains("$")&&fileName.endsWith(".class")) {
                clzCompleteNameList.add(fileName.replace(basePath, "").replace("/", ".").replace(".class", ""));
            }
        }
    }

}
