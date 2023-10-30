package org.example.sensitiveword;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author huang
 */
@Component
public class FileWordAllow implements IWordAllow {
    @Override
    public List<String> allow() {
        try {
            return FileUtils.readLines(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("wordAllow.txt")).getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
