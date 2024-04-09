package org.example.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class WordTableParser {

    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\asus\\Desktop\\table.docx"; // 替换为您的Word文件路径

        try (FileInputStream fis = new FileInputStream(filePath)) {
            XWPFDocument document = new XWPFDocument(fis);

            // 获取文档中的第一个表格（如果有多个表格，需要遍历或根据索引选择）
            XWPFTable table = document.getTables().get(0);

            // 遍历表格行
            for (XWPFTableRow row : table.getRows()) {
                // 遍历表格单元格
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    String cellValue = cell.getText(); // 获取单元格文本内容

                    System.out.print(cellValue + "\t"); // 输出单元格值，这里使用制表符分隔各单元格
                }
                System.out.println(); // 换行，表示一行结束
            }
        }
    }
}
