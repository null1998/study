package org.example.service.parser;

import org.example.common.TypeConstants;
import org.springframework.stereotype.Service;

/**
 * @author huang
 */
@Service(TypeConstants.JSON_PARSER)
public class JSONParser implements Parser {
    @Override
    public String parse(String data) {
        return "json";
    }
}
