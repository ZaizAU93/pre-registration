package com.example.scammer.controllers;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/schedule")
public class ExcelController {

    private static final String UPLOAD_DIR = "uploads";

    @GetMapping
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        } else {
            // Удаляем все файлы в папке uploads
            try (Stream<Path> files = Files.list(uploadPath)) {
                files.forEach(f -> {
                    try {
                        Files.deleteIfExists(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        String filename = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        if (Files.exists(filePath)) {
            try (InputStream is = Files.newInputStream(filePath);
                 Workbook workbook = WorkbookFactory.create(is)) {
                List<List<CellData>> sheetData = processWorkbook(workbook, filename);
                model.addAttribute("sheetData", sheetData);
                model.addAttribute("uploadedFileName", filename);
            }
        } else {
            Files.copy(file.getInputStream(), filePath);
            try (InputStream is = Files.newInputStream(filePath);
                 Workbook workbook = WorkbookFactory.create(is)) {
                List<List<CellData>> sheetData = processWorkbook(workbook, filename);
                model.addAttribute("sheetData", sheetData);
                model.addAttribute("uploadedFileName", filename);
            }
        }
        return "display"; // или нужная страница
    }

    @GetMapping("/graph")
    public String showGraphPage(Model model) {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            model.addAttribute("sheetData", new ArrayList<>());
            return "graph";
        }

        try (Stream<Path> files = Files.list(uploadPath)) {
            Path latestFile = files
                    .filter(Files::isRegularFile)
                    .max(Comparator.comparingLong(f -> {
                        try {
                            return Files.getLastModifiedTime(f).toMillis();
                        } catch (IOException e) {
                            return 0L;
                        }
                    }))
                    .orElse(null);

            if (latestFile != null && Files.exists(latestFile)) {
                try (InputStream is = Files.newInputStream(latestFile);
                     Workbook workbook = WorkbookFactory.create(is)) {
                    List<List<CellData>> sheetData = processWorkbook(workbook, latestFile.getFileName().toString());
                    model.addAttribute("sheetData", sheetData);
                }
            } else {
                model.addAttribute("sheetData", new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("sheetData", new ArrayList<>());
        }
        return "graph";
    }

    private List<List<CellData>> processWorkbook(Workbook workbook, String filename) {
        List<List<CellData>> sheetData = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        int maxColumnCount = 0;
        DataFormatter dataFormatter = new DataFormatter();

        // Первый проход: найти последний заполненный столбец в каждой строке
        List<Integer> lastFilledCells = new ArrayList<>();
        for (Row row : sheet) {
            int lastFilled = -1;
            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    lastFilled = cn;
                }
            }
            lastFilledCells.add(lastFilled);
        }

        // Найти максимум
        for (Integer lastIndex : lastFilledCells) {
            if (lastIndex != -1 && lastIndex + 1 > maxColumnCount) {
                maxColumnCount = lastIndex + 1;
            }
        }

        // Обработка строк
        for (Row row : sheet) {
            List<CellData> rowData = new ArrayList<>();
            for (int cn = 0; cn < maxColumnCount; cn++) {
                Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                CellData cellData = new CellData();

                if (cell.getCellType() != CellType.BLANK) {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellData.setValue(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                cellData.setValue(cell.getDateCellValue().toString());
                            } else {
                                String formattedNumber = dataFormatter.formatCellValue(cell);
                                cellData.setValue(formattedNumber);
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
                } else {
                    cellData.setValue("");
                }

                // Цвет фона
                CellStyle style = cell.getCellStyle();
                String bgColorHex = null;

                if (style != null) {
                    if (style instanceof XSSFCellStyle) {
                        XSSFCellStyle xssfStyle = (XSSFCellStyle) style;
                        XSSFColor color = xssfStyle.getFillForegroundXSSFColor();
                        if (color != null && !color.isAuto()) {
                            String argbHex = color.getARGBHex();
                            if (argbHex != null && argbHex.length() == 8) {
                                bgColorHex = "#" + argbHex.substring(2);
                            }
                        }
                    } else if (style instanceof HSSFCellStyle) {
                        HSSFCellStyle hssfStyle = (HSSFCellStyle) style;
                        short fillForegroundColor = hssfStyle.getFillForegroundColor();
                        if (fillForegroundColor != 0) {
                            HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
                            HSSFColor color = palette.getColor(fillForegroundColor);
                            if (color != null) {
                                short[] triplet = color.getTriplet();
                                if (triplet != null && triplet.length == 3) {
                                    bgColorHex = String.format("#%02X%02X%02X", triplet[0], triplet[1], triplet[2]);
                                }
                            }
                        }
                    }
                }

                if (bgColorHex != null) {
                    cellData.setBgColor(bgColorHex);
                }

                rowData.add(cellData);
            }
            sheetData.add(rowData);
        }

        return sheetData;
    }


    // Внутренний класс
    public static class CellData {
        private String value;
        private String bgColor;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getBgColor() { return bgColor; }
        public void setBgColor(String bgColor) { this.bgColor = bgColor; }
    }
}
