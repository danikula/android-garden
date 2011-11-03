package com.danikula.android16.core.transport;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class XmlAbstractResponseParser<T> extends AbstractResponseParser<T> {

    public T parseResponse(InputStream serverResponse) throws ResponseParsingException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(serverResponse);
            return parseXml(document);
        }
        catch (IOException e) {
            throw new ResponseParsingException("Error parsing xml based server response", e);
        }
        catch (SAXException e) {
            throw new ResponseParsingException("Error parsing xml based server response", e);
        }
        catch (ParserConfigurationException e) {
            throw new ResponseParsingException("Error parsing xml based server response", e);
        }
    }

    protected abstract T parseXml(Document document);

    /**
     * Извлекает текст из текстового узла.
     * 
     * @param node Node узел xml-документа, который должен содержать только текст
     * @return String текст из текстового узла
     */
    protected String getNodeTextValue(Node node) {
        Node textContent = node.getFirstChild();
        // if node contain something similar ]¤Ñß]@[6 4 À Ï, then parser return null instead of node's text
        return textContent != null ? textContent.getNodeValue().trim() : "";
    }

    protected int getNodeIntValue(Node node) {
        String strValue = getNodeTextValue(node);
        return Integer.parseInt(strValue);
    }
}
