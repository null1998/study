package org.example.sensitiveword;

import com.github.houbb.sensitive.word.api.IWordDeny;
import org.example.dao.SensitiveWordMapper;
import org.example.entity.SensitiveWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 黑名单
 *
 * @author huang
 */
@Component
public class DataBaseWordDeny implements IWordDeny {
    private SensitiveWordMapper sensitiveWordMapper;

    @Autowired
    public DataBaseWordDeny(SensitiveWordMapper sensitiveWordMapper) {
        this.sensitiveWordMapper = sensitiveWordMapper;
    }

    @Override
    public List<String> deny() {
        return sensitiveWordMapper.query(1).stream().map(SensitiveWord::getValue).collect(Collectors.toList());
    }
}
