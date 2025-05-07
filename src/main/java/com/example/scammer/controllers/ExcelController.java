package com.example.scammer.controllers;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ExcelController {

    @GetMapping
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        List<List<CellData>> sheetData = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<CellData> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    CellData cellData = new CellData();

                    // Получение значения
                    switch (cell.getCellType()) {
                        case STRING:
                            cellData.setValue(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                cellData.setValue(cell.getDateCellValue().toString());
                            } else {
                                cellData.setValue(Double.toString(cell.getNumericCellValue()));
                            }
                            break;
                        case BOOLEAN:
                            cellData.setValue(Boolean.toString(cell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            CellValue evaluatedValue = evaluator.evaluate(cell);
                            cellData.setValue(evaluatedValue.formatAsString());
                            break;
                        default:
                            cellData.setValue("");
                    }

                    // Получение стилей
                    CellStyle style = cell.getCellStyle();
                    if (style != null) {
                        // Проверяем тип стиля
                        if (style instanceof XSSFCellStyle) {
                            XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
                            XSSFColor color = xssfStyle.getFillBackgroundColorColor();
                            if (color != null) {
                                String hexColor = color.getARGBHex();
                                if (hexColor != null && !hexColor.isEmpty()) {
                                    // Убираем альфа-канал (если есть)
                                    cellData.setBgColor("#" + hexColor.substring(2));
                                }
                            }
                            // Можно добавить обработку шрифтов, границ и других стилей
                        } else {
                            // Для HSSF (XLS) можно получать цвета через другие методы
                            short fillForegroundColor = style.getFillForegroundColor();
                            if (fillForegroundColor != 0) {
                                // Получение цвета из палитры
                                HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
                                HSSFColor color = palette.getColor(fillForegroundColor);
                                if (color != null) {
                                    short[] rgb = color.getTriplet();
                                    String hexColor = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
                                    cellData.setBgColor(hexColor);
                                }
                            }
                        }
                    }

                    rowData.add(cellData);
                }
                sheetData.add(rowData);
            }
        }

        model.addAttribute("sheetData", sheetData);
        return "display";
    }


    // Внутренний класс для хранения данных о ячейке
    public static class CellData {
        private String value;
        private String bgColor; // В формате ARGB, например "#FF0000"

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }
    }
}