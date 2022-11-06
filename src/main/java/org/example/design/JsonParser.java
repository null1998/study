package org.example.design;

public class JsonParser {
    public void parse(JsonData jsonData) {
        System.out.println(jsonData.getContent());
    }
}

class JsonData {
    private String content;

    public String getContent() {
        return content;
    }
}

class XmlData {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

class XmlAdapter extends JsonData {
    private final XmlData xmlData;

    public XmlAdapter(XmlData xmlData) {
        this.xmlData = xmlData;
    }

    @Override
    public String getContent() {
        return convert(xmlData.getContent());
    }

    private String convert(String content) {
        return content;
    }
}