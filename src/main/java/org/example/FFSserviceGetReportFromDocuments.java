package org.example;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FFSserviceGetReportFromDocuments {
    public static void main( String[] args ) throws IOException {
        String path = "D:\\Отчеты\\";
        String pathDocuments = path + "Документы\\";
        if (Files.isDirectory(Paths.get(pathDocuments))) {
            File file = new File(pathDocuments);
            for (File f: file.listFiles()) {
                PdfReader reader = new PdfReader(pathDocuments + "\\" + f.getName() + "\\" + f.getName() + ".pdf");
                int pages = reader.getNumberOfPages();
                StringBuilder text = new StringBuilder();
                for (int i = 1; i <= pages; i++) {
                    text.append(PdfTextExtractor.getTextFromPage(reader, i));
                }
                reader.close();
                System.out.println(text);
            }
        }
    }
}
