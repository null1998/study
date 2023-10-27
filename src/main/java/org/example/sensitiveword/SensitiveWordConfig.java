package org.example.sensitiveword;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * 敏感词配置
 *
 * @author huang
 */
@Configuration
public class SensitiveWordConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(SensitiveWordConfig.class);
    @Bean
    public SensitiveWordBs sensitiveWordBs(IWordDeny wordDeny, IWordAllow wordAllow) {
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

    /**
     * 自定义黑名单
     *
     * @return {@link IWordDeny}
     */
    @Bean
    public IWordDeny wordDeny() throws IOException {
        List<String> wordDenyList = FileUtils.readLines(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("wordDeny.txt")).getPath()), StandardCharsets.UTF_8);
        return () -> wordDenyList;
    }

    /**
     * 自定义白名单
     *
     * @return {@link IWordAllow}
     */
    @Bean
    public IWordAllow wordAllow() throws IOException {
        List<String> wordAllowList = FileUtils.readLines(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("wordAllow.txt")).getPath()), StandardCharsets.UTF_8);
        return () -> wordAllowList;
    }
}
