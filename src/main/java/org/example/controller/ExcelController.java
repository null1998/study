package org.example.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.example.entity.ExcelDataDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huang
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("excel导出文件", StandardCharsets.UTF_8.name()) + ExcelTypeEnum.XLSX.getValue());
        List<ExcelDataDTO> excelDataList = getExcelData();
        EasyExcel.write(response.getOutputStream())
                .head(ExcelDataDTO.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("excel导出")
                .doWrite(excelDataList);
    }

    private List<ExcelDataDTO> getExcelData() {
        List<ExcelDataDTO> excelDataList = new ArrayList<>();
        ExcelDataDTO excelData = new ExcelDataDTO();
        excelData.setName("张三");
        excelData.setAmt(new BigDecimal("3.66"));
        excelData.setDate(LocalDate.of(2023,1,1));
        excelDataList.add(excelData);
        ExcelDataDTO excelData1 = new ExcelDataDTO();
        excelData1.setName("李四");
        excelData1.setAmt(new BigDecimal("5.00"));
        excelData1.setDate(LocalDate.of(2024,1,1));
        excelDataList.add(excelData1);
        return excelDataList;
    }
}
