package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtils {
    public static void printText(String filePath) {
        try (InputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Получаем первый лист
            Sheet sheet = workbook.getSheetAt(0);

            // Итерация по строкам и ячейкам листа
            for (Row row : sheet) {
                for (Cell cell : row) {
                    // Получаем значение ячейки в зависимости от типа данных
                    switch (cell.getCellType()) {
                        case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                        case NUMERIC -> {
                            if (DateUtil.isCellDateFormatted(cell)) {
                                System.out.print(cell.getDateCellValue() + "\t");
                            } else {
                                System.out.print(cell.getNumericCellValue() + "\t");
                            }
                        }
                        case BOOLEAN -> System.out.print(cell.getBooleanCellValue() + "\t");
                        case FORMULA -> System.out.print(cell.getCellFormula() + "\t");
                        default -> System.out.print("UNKNOWN\t");
                    }
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean searchText(String filePath, String textToSearch) {
        try (InputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Получаем первый лист
            Sheet sheet = workbook.getSheetAt(0);

            // Итерация по строкам и ячейкам листа
            for (Row row : sheet) {
                for (Cell cell : row) {
                    // Получаем значение ячейки
                    if (cell.getCellType().equals(CellType.STRING)) {
                        String text = cell.getStringCellValue();
                        if (text.equals(textToSearch)) {
                            return true;
                        }
                    }
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
