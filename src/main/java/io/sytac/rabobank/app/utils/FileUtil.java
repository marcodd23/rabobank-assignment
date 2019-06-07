package io.sytac.rabobank.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sytac.rabobank.app.model.ReportItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public static void writeReportToJsonFile(List<ReportItem> report) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(new File("jsonReport.json"));
            gson.toJson(report, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("#### - Error priting report!!");
            e.printStackTrace();
        }
    }
}
