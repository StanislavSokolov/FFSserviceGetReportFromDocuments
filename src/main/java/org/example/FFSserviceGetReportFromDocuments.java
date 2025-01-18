package org.example;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FFSserviceGetReportFromDocuments {
    public static void main( String[] args ) throws IOException {
        String path = "D:\\Отчеты\\";
        String pathDocuments = path + "Документы\\";
        double sum = 0;
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
                String st = "";
                if (text.indexOf("Всего к оплате") != -1) {
                    // Выделяем строку
                    st = text.substring(text.indexOf("Всего к оплате"), text.indexOf("Руководитель"));
                    // Разделяем на слова
                    String[] rows = st.split(" ");
                    // Выделяем нужную ячейку
                    String row = rows[6];
                    // Меняем знак
                    row = row.replace(",", ".");
                    // Суммируем с предыдущим значением
                    sum = sum + Double.valueOf(row);
                    System.out.println(f.getName() + " - " + Double.valueOf(row) + " рублей");
                }
            }
        }
        System.out.println("");
        System.out.println("Итого: " + sum + " рублей");
    }
}
