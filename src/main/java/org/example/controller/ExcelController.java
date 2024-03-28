package org.example.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import org.example.entity.ExcelDataDTO;
import org.example.util.ExcelUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @GetMapping("/exportSum")
    public void exportSum(HttpServletResponse response) {
        List<ExcelDataDTO> excelDataList = getExcelData();
        ExcelDataDTO excelDataSumDTO = new ExcelDataDTO();
        excelDataSumDTO.setName("合计");
        excelDataSumDTO.setAmt(excelDataList.stream().map(ExcelDataDTO::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.DOWN));
        ExcelUtil.exportExcel("excel导出", ExcelDataDTO.class, excelDataList, Lists.newArrayList(excelDataSumDTO), response);
    }

    private List<ExcelDataDTO> getExcelData() {
        List<ExcelDataDTO> excelDataList = new ArrayList<>();
        ExcelDataDTO excelData = new ExcelDataDTO();
        excelData.setName("张三");
        excelData.setAmt(new BigDecimal("3.66"));
        excelData.setDate(LocalDate.of(2023, 1, 1));
        excelDataList.add(excelData);
        ExcelDataDTO excelData1 = new ExcelDataDTO();
        excelData1.setName("李四");
        excelData1.setAmt(new BigDecimal("5.00"));
        excelData1.setDate(LocalDate.of(2024, 1, 1));
        excelDataList.add(excelData1);
        return excelDataList;
    }
}
