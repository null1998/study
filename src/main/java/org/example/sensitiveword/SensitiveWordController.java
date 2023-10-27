package org.example.sensitiveword;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.result.WordResultHandlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huang
 */
@RestController
@RequestMapping("/sensitive/word")
public class SensitiveWordController {
    private SensitiveWordBs sensitiveWordBs;

    @Autowired
    public SensitiveWordController(SensitiveWordBs sensitiveWordBs) {
        this.sensitiveWordBs = sensitiveWordBs;
    }

    /**
     * 敏感词检测接口
     *
     * @param checkParam 原始文本
     * @return {@link CheckResult}
     */
    @PostMapping("/check")
    public CheckResult check(@RequestBody CheckParam checkParam) {
        String originText = checkParam.getOriginText();
        CheckResult checkResult = new CheckResult(originText);
        if (sensitiveWordBs.contains(originText)) {
            checkResult.setContainSensitiveWord(true);
            checkResult.setSensitiveWords(sensitiveWordBs.findAll(originText, WordResultHandlers.word()));
            checkResult.setAfterReplaceText(sensitiveWordBs.replace(originText));
        }
        return checkResult;
    }

    /**
     * 自定义黑名单、白名单刷新接口
     */
    @GetMapping("/refresh")
    public void refresh() {
        sensitiveWordBs.init();
    }
}
