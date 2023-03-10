package com.viettel.vtskit.easypoi.poi.common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Handle cell values
 *
 */
public class PoiCellUtil {
    /**
     * read cell value
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getCellValue(Sheet sheet, int row, int column) {
        String value = null;
        if (isMergedRegion(sheet, row, column)) {
            value = getMergedRegionValue(sheet, row, column);
        } else {
            Row rowData = sheet.getRow(row);
            Cell cell = rowData.getCell(column);
            value = getCellValue(cell);
        }
        return value;
    }

    /**
     * Get the value of the merged cell
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);

                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    /**
     * Determine whether the specified cell is a merged cell
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get the value of the cell
     * _NONE(-1),
     * NUMERIC(0),
     * STRING(1),
     * FORMULA(2),
     * BLANK(3),
     * BOOLEAN(4),
     * ERROR(5);
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            try {
                return cell.getCellFormula();
            } catch (Exception e) {
                return String.valueOf(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            return String.valueOf(cell.getErrorCellValue());
        }
        return "";
    }

}
