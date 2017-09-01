package raspored;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author luka
 */
public class IO {

    private static String sheetName[];


    /**
     * Kill it. Kill it with fire.
     *
     * @throws IOException
     */
    public static void readXSSF() throws IOException {
        int brSheet, brRed, brKol, i, k;
        DecimalFormat df = new DecimalFormat("#.00");
        String[][] matrix;
        FileInputStream file = new FileInputStream(Raspored.podaciFile);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        brSheet = workbook.getNumberOfSheets();
        XSSFSheet sheets[] = new XSSFSheet[brSheet];
        sheetName = new String[brSheet];
        Raspored.podaci = new String[brSheet][][];
        for (int l = 0; l < brSheet; l++) {
            sheets[l] = workbook.getSheetAt(l);
            sheetName[l] = sheets[l].getSheetName();
        }

        for (i = 0; i < brSheet; i++) {
            k = 0;
            brRed = sheets[i].getLastRowNum();
            Iterator<Row> rowIterator = sheets[i].iterator();
            Row row = rowIterator.next();
            brKol = row.getLastCellNum();
            System.out.println(" Sheet " + i + "    Redova  =  " + (brRed));
            System.out.println(" Sheet " + i + "    Kolona   =  " + brKol);
            matrix = new String[brKol][brRed + 1];
            Iterator<Cell> cellIterator = row.cellIterator();
            try {
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    try {
                        matrix[cell.getColumnIndex()][0] = cell.getStringCellValue();
                    } catch (IllegalStateException ex) {
                        matrix[cell.getColumnIndex()][0] = df.format(cell.getNumericCellValue());
                    }
                }
                while (k < brRed) {
                    row = rowIterator.next();
                    k++;
                    cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getColumnIndex() <= brKol)
                            try {
                                matrix[cell.getColumnIndex()][k] = cell.getStringCellValue();
                            } catch (IllegalStateException ex) {
                                matrix[cell.getColumnIndex()][k] = String.valueOf(cell.getNumericCellValue());
                            } catch (IndexOutOfBoundsException ex) {
                                System.out.println("ex: IndexOutOfBounds @ IO:77");
                            }
                    }
                }
            } catch (NoSuchElementException ex) {
            }
            Raspored.podaci[i] = matrix;
        }
        file.close();
    }

    public static void readHSSF() throws IOException {
        int brSheet, brRed, brKol, i = 0, k = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        String[][] matrix;
        FileInputStream file = new FileInputStream(Raspored.podaciFile);
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        brSheet = workbook.getNumberOfSheets();
        HSSFSheet sheets[] = new HSSFSheet[brSheet];
        sheetName = new String[brSheet];


        Raspored.podaci = new String[brSheet][][];
        for (int l = 0; l < brSheet; l++) {
            sheets[l] = workbook.getSheetAt(l);
            sheetName[l] = sheets[l].getSheetName();
        }

        for (i = 0; i < brSheet; i++) {
            k = 0;
            brRed = sheets[i].getLastRowNum();
            Iterator<Row> rowIterator = sheets[i].iterator();
            Row row = rowIterator.next();
            brKol = row.getLastCellNum();
            matrix = new String[brKol][brRed + 1];
            System.out.println(" Sheet " + i + "   Broj Redova  =  " + brRed);
            System.out.println(" Sheet " + i + "   Broj Kolona   =  " + brKol);
            Iterator<Cell> cellIterator = row.cellIterator();
            try {
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    try {
                        matrix[cell.getColumnIndex()][0] = cell.getStringCellValue();
                    } catch (IllegalStateException ex) {
                        matrix[cell.getColumnIndex()][0] = df.format(cell.getNumericCellValue());
                    }
                }
                while (k < brRed) {
                    row = rowIterator.next();
                    k++;
                    cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        try {
                            matrix[cell.getColumnIndex()][k] = cell.getStringCellValue();
                        } catch (IllegalStateException ex) {
                            matrix[cell.getColumnIndex()][k] = String.valueOf(cell.getNumericCellValue());
                        }
                    }
                }
            } catch (NoSuchElementException ex) {
            }
            Raspored.podaci[i] = matrix;
        }
        file.close();
    }

    public static void writeHSSF() throws IOException {
        Raspored.outFile.createNewFile();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        for (int j = 0; j < Raspored.out.length; j++) {
            Row row = sheet.createRow(j);
            for (int k = 0; k < Raspored.out[j].length; k++) {
                Cell cell = row.createCell(k);
                cell.setCellValue(Raspored.out[k][j]);
            }
        }
        for (int i = 0; i < Raspored.out[0].length; i++) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream out
                = new FileOutputStream(Raspored.outFile);
        workbook.write(out);
        out.close();
    }


    public static void writeXSSF() throws IOException {
        Raspored.outFile.createNewFile();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        for (int j = 0; j < Raspored.out.length; j++) {
            Row row = sheet.createRow(j);
            for (int k = 0; k < Raspored.out[j].length; k++) {
                Cell cell = row.createCell(k);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(Raspored.out[j][k]);
            }
        }
        for (int i = 0; i < Raspored.out[0].length; i++) {
            if (Raspored.out[2][i] != null && Raspored.out[2][i].length() > 3)
                sheet.setColumnWidth(i, 6000);
            else
                sheet.autoSizeColumn(i);
        }
        FileOutputStream out
                = new FileOutputStream(Raspored.outFile);
        workbook.write(out);
        out.close();
    }
}
