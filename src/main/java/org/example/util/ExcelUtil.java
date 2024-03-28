package org.example.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author huang
 */
public class ExcelUtil {

    /**
     * http导出excel文件，带有合计行
     *
     * @param fileName      文件名
     * @param clazz         excel业务数据class对象
     * @param excelDataList excel业务数据
     * @param excelSumList  excel业务数据合计行
     * @param response      响应流
     */
    public static <T> void exportExcel(String fileName, Class<T> clazz, List<T> excelDataList, List<T> excelSumList, HttpServletResponse response) {
        try {
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).needHead(Boolean.FALSE).build();
            // 第一个表格，存放业务数据
            WriteTable writeTable0 = EasyExcel.writerTable(0).needHead(Boolean.TRUE).build();
            // 使用绿色突出合计行样式
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(null, contentWriteCellStyle);
            // 第二个表格，去掉表头存放合计行数据
            WriteTable writeTable1 = EasyExcel.writerTable(1).needHead(Boolean.FALSE).registerWriteHandler(horizontalCellStyleStrategy).build();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + ExcelTypeEnum.XLSX.getValue());
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
                    .autoCloseStream(false)
                    // 宽度自适应
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .build();
            excelWriter.write(excelDataList, writeSheet, writeTable0);
            excelWriter.write(excelSumList, writeSheet, writeTable1);
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("生成" + fileName + "excel文件失败");
        }
    }
}
