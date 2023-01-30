package org.example.common;

/**
 * @author huang
 */
public enum ParserType {
    JSON(TypeConstants.JSON_PARSER),
    XML(TypeConstants.XML_PARSER);
    private final String parserName;

    ParserType(String parserName) {
        this.parserName = parserName;
    }

    @Override
    public String toString() {
        return this.parserName;
    }
}

