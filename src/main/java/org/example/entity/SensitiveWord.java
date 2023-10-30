package org.example.entity;

/**
 * 敏感词
 *
 * @author huang
 */
public class SensitiveWord {
    /**
     * 主键
     */
    private String id;
    /**
     * 类型1、禁用词，2、非禁用词
     */
    private Integer type;
    /**
     * 值
     */
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
