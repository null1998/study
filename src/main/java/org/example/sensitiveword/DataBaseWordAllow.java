package org.example.sensitiveword;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.example.dao.SensitiveWordMapper;
import org.example.entity.SensitiveWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 白名单
 *
 * @author huang
 */
@Component
public class DataBaseWordAllow implements IWordAllow {
    private SensitiveWordMapper sensitiveWordMapper;

    @Autowired
    public DataBaseWordAllow(SensitiveWordMapper sensitiveWordMapper) {
        this.sensitiveWordMapper = sensitiveWordMapper;
    }

    @Override
    public List<String> allow() {
        return sensitiveWordMapper.query(2).stream().map(SensitiveWord::getValue).collect(Collectors.toList());
    }
}
