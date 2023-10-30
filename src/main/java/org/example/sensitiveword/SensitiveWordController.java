package org.example.sensitiveword;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.result.WordResultHandlers;
import org.example.dao.SensitiveWordMapper;
import org.example.entity.SensitiveWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huang
 */
@RestController
@RequestMapping("/sensitive/word")
public class SensitiveWordController {
    private SensitiveWordBs sensitiveWordBs;

    private SensitiveWordMapper sensitiveWordMapper;

    @Autowired
    public SensitiveWordController(SensitiveWordBs sensitiveWordBs, SensitiveWordMapper sensitiveWordMapper) {
        this.sensitiveWordBs = sensitiveWordBs;
        this.sensitiveWordMapper = sensitiveWordMapper;
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

    /**
     * 保存敏感词配置
     *
     * @param sensitiveWordList 敏感词配置
     */
    @PostMapping("/save")
    public void save(@RequestBody List<SensitiveWord> sensitiveWordList) {
        sensitiveWordMapper.save(sensitiveWordList);
    }

    /**
     * 删除敏感词配置
     *
     * @param idList 主键列表
     */
    @PostMapping("/delete")
    public void delete(@RequestBody List<String> idList) {
        sensitiveWordMapper.delete(idList);
    }
}
