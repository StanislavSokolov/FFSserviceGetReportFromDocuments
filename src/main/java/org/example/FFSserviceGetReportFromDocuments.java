package org.example;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;

public class FFSserviceGetReportFromDocuments {
    public static void main( String[] args ) throws IOException {
        String path = "D:\\Отчеты\\";
        String pathDocuments = path + "Документы\\";
        String fileName = pathDocuments + "УПД №011200228202 от 12.01.2025\\" + "УПД №011200228202 от 12.01.2025" + ".pdf";
//        BufferedReader reader = new BufferedReader(new FileReader(fileName));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//        }
//        reader.close();
        PdfReader reader = new PdfReader(fileName);
        int pages = reader.getNumberOfPages();
        StringBuilder text = new StringBuilder();
        for (int i = 1; i <= pages; i++) {
            text.append(PdfTextExtractor.getTextFromPage(reader, i));
        }
        reader.close();
        System.out.println(text);
    }
}
