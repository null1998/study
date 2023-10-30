package org.example.sensitiveword;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 敏感词配置
 *
 * @author huang
 */
@Configuration
public class SensitiveWordConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(SensitiveWordConfig.class);
    @Bean
    public SensitiveWordBs sensitiveWordBs(@Qualifier("dataBaseWordDeny") IWordDeny wordDeny, @Qualifier("dataBaseWordAllow") IWordAllow wordAllow) {
        LOGGER.info("SensitiveWordBs初始化");
        LocalDateTime time = LocalDateTime.now();
        SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance()
                .ignoreCase(true)
                .ignoreWidth(true)
                .ignoreNumStyle(true)
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .ignoreRepeat(false)
                .enableNumCheck(true)
                .enableEmailCheck(true)
                .enableUrlCheck(true)
                .enableWordCheck(true)
                .numCheckLen(8)
                .wordDeny(WordDenys.chains(WordDenys.defaults(), wordDeny))
                .wordAllow(WordAllows.chains(WordAllows.defaults(), wordAllow))
                .init();
        LOGGER.info("SensitiveWordBs初始化完毕，耗时{}毫秒", ChronoUnit.MILLIS.between(time, LocalDateTime.now()));
        return sensitiveWordBs;
    }
}
