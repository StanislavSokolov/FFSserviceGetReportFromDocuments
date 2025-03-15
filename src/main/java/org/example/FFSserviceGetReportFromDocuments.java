package org.example;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FFSserviceGetReportFromDocuments {
    public static void main( String[] args ) throws IOException {
//        String path = "D:\\Отчеты\\";
//        String pathDocuments = path + "Документы\\";
//        String pathFiltredDocuments = path + "Отчеты\\";

        String path = "/Документы/Отчеты/";
        String pathDocuments = path + "Документы/";
        String pathFiltredDocuments = path + "Отчеты/";

//        String start = "01.01.2025";
//        String stop = "31.03.2025";
        String start = args[0];
        String stop = args[1];

        String[] rowsStart = start.split("\\.");
//        int dayStart = Integer.parseInt(rowsStart[0]);
        int monthStart = Integer.parseInt(rowsStart[1]);
        int yearStart = Integer.parseInt(rowsStart[2]);

        String[] rowsStop = stop.split("\\.");
//        int dayStop = Integer.parseInt(rowsStop[0]);
        int monthStop = Integer.parseInt(rowsStop[1]);
        int yearStop = Integer.parseInt(rowsStop[2]);

        String newDir = pathFiltredDocuments + start + "-" + stop;
        File fileFiltred = new File(newDir);

        if (!fileFiltred.exists()) {
            boolean isCreated = fileFiltred.mkdirs(); // mkdirs() создает все необходимые промежуточные директории
            if (isCreated) {
                System.out.println("Директория для отчета успешно создана: " + newDir);
            } else {
                System.out.println("Не удалось создать директорию для отчета: " + newDir);
            }
        } else {
            System.out.println("Директория уже существует: " + newDir);
            File[] files = fileFiltred.listFiles();
                if (files != null) {
                    for (File file: files) {
                        deleteRecursively(file);
                    }
                }
            System.out.println("Удаляем ее содержимое");
        }

        if (Files.isDirectory(Paths.get(pathDocuments))) {
            File file = new File(pathDocuments);
            for (File f : file.listFiles()) {
                if (f.getName().contains("УПД №")) {
                    String st = f.getName();
                    String[] rows = st.split(" ");
                    if (rows.length == 4) {
                        String row = rows[rows.length - 1];
//                        System.out.println(row);
                        String[] rows1 = row.split("\\.");
                        int day = Integer.parseInt(rows1[0]);
                        int month = Integer.parseInt(rows1[1]);
                        int year = Integer.parseInt(rows1[2]);
                        if (year >= yearStart & year <= yearStop) {
                            if (month >= monthStart & month <= monthStop) {
//                                Files.copy(Paths.get(f.getAbsolutePath() + "\\" + f.getName() + ".pdf"), Paths.get(fileFiltred.getAbsolutePath(), f.getName() + ".pdf"));
                                Files.copy(Paths.get(f.getAbsolutePath() + "/" + f.getName() + ".pdf"), Paths.get(fileFiltred.getAbsolutePath(), f.getName() + ".pdf"));
                            }
                        }
                    }

                }
            }
        }

        int cnt = 0;
        double sum = 0.0;
        if (Files.isDirectory(Paths.get(newDir))) {
            File file = new File(newDir);
            for (File f: file.listFiles()) {
                if (f.getName().contains("УПД №")) {
//                    System.out.println(f.getName());
//                    PdfReader reader = new PdfReader(newDir + "\\" + f.getName());
                    PdfReader reader = new PdfReader(newDir + "/" + f.getName());
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
//                        System.out.println(st);
                        // Разделяем на слова
                        String[] rows = st.split(" ");
                        // Выделяем нужную ячейку
                        String row = rows[rows.length - 1];
//                        System.out.println(row);
                        String[] rows1 = row.split("\\n");
//                        System.out.println("dfv" + rows1[0]);
                        // Меняем знак
                        rows1[0] = rows1[0].replace(",", ".");
                        // Суммируем с предыдущим значением
//                        System.out.println(rows1[0]);
//                        System.out.println(Double.valueOf(rows1[0]));
                        sum = sum + Double.valueOf(rows1[0]);
                        System.out.println(f.getName() + " - " + Double.valueOf(rows1[0]) + " рублей");
                    }
                }
            }
            cnt = file.listFiles().length;
        }
        System.out.println("");
        System.out.println("Итого: " + sum + " рублей");
        System.out.println("Просмотрено " + cnt + " документов");
    }

    private static void deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    deleteRecursively(subFile);
                }
            }
        }
        if (!file.delete()) {
            System.err.println("Не удалось удалить файл или директорию: " + file.getAbsolutePath());
        }
    }
}
