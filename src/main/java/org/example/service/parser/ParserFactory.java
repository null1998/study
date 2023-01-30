package org.example.service.parser;

import org.example.common.ParserType;

/**
 * @author huang
 */
public interface ParserFactory {
    Parser getParser(ParserType parserType);
}
