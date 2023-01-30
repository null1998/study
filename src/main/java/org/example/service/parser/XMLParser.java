package org.example.service.parser;

import org.example.common.TypeConstants;
import org.springframework.stereotype.Component;

/**
 * @author huang
 */
@Component(TypeConstants.XML_PARSER)
public class XMLParser implements Parser {
    @Override
    public String parse(String data) {
        return "xml";
    }
}
