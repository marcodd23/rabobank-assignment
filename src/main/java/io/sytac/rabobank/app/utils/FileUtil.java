package io.sytac.rabobank.app.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtil {

    private static final String basePath = "input/";


    public static Reader getReader(String fileName){
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new ClassPathResource(basePath + fileName).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStreamReader;
    }

/*    public static String createFileUseJavaNewIO(String filePath)
    {
        String fileAbsoultePath = "";
        try
        {
            *//* First create the Path object. *//*
            Path filePathObj = Paths.get(filePath);

            Path result = Files.createFile(filePathObj);

            System.out.println("Create " + filePath + " success. ");

            *//* Show absolute path to see different filePath case real file location.*//*
            fileAbsoultePath = result.toFile().getAbsolutePath();
            System.out.println("File absolute path is " + fileAbsoultePath);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return fileAbsoultePath;
    }
    *//* Test createFileUseJavaNewIO(String filePath) method with three scenario.
     * case1 : file name only.
     * case2 : absolute path.
     * case3 : relative path.
     * case1 and case3 will create the file under project root folder or current java class run folder.
     * *//*
    public static void createFileUseJavaNewIOExample()
    {
        *//* Create with only file name. *//*
        String fileName = "dev2qa.com.txt";
        createFileUseJavaNewIO(fileName);

        *//* Get path separator to make java code platform independent. *//*
        String fileSep = System.getProperty("file.separator");

        *//* Create with absolute path. *//*

        *//* Get current working directory. *//*
        String currDir = System.getProperty("user.dir");
        *//* Build a absolute path. *//*
        String absoluteFilePath = currDir + fileSep + "test" + fileSep + "dev2qa.com.txt";
        createFileUseJavaNewIO(absoluteFilePath);

        *//* Create with relative path. *//*
        String relativeFilePath = "testRelative" + fileSep + "dev2qa.com.txt";
        createFileUseJavaNewIO(relativeFilePath);
    }*/
}
