package org.example.sensitiveword;

import java.util.List;

/**
 * 敏感词检测结果
 *
 * @author huang
 */
public class CheckResult {
    /**
     * 是否包含敏感词
     */
    private boolean containSensitiveWord;
    /**
     * 所有敏感词
     */
    private List<String> sensitiveWords;
    /**
     * 原始文本
     */
    private String originText;
    /**
     * 替换后的文本
     */
    private String afterReplaceText;

    public CheckResult(String originText) {
        this.originText = originText;
    }

    public boolean isContainSensitiveWord() {
        return containSensitiveWord;
    }

    public void setContainSensitiveWord(boolean containSensitiveWord) {
        this.containSensitiveWord = containSensitiveWord;
    }

    public List<String> getSensitiveWords() {
        return sensitiveWords;
    }

    public void setSensitiveWords(List<String> sensitiveWords) {
        this.sensitiveWords = sensitiveWords;
    }

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public String getAfterReplaceText() {
        return afterReplaceText;
    }

    public void setAfterReplaceText(String afterReplaceText) {
        this.afterReplaceText = afterReplaceText;
    }
}
