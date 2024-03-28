package org.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author huang
 */
public class ExcelDataDTO {
    @ExcelProperty(value = "名字", index = 0)
    private String name;
    @ExcelProperty(value = "金额", index = 1)
    private BigDecimal amt;
    @ExcelProperty(value = "日期", index = 2)
    private LocalDate date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
