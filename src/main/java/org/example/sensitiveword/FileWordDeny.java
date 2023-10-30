package org.example.sensitiveword;

import com.github.houbb.sensitive.word.api.IWordDeny;
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
public class FileWordDeny implements IWordDeny {
    @Override
    public List<String> deny() {
        try {
            return FileUtils.readLines(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("wordDeny.txt")).getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
