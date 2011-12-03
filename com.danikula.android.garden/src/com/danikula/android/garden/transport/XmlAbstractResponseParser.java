package com.danikula.android.garden.transport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.danikula.android.garden.io.IoUtils;

public abstract class XmlAbstractResponseParser<T> extends AbstractResponseParser<T> {

    public T parseResponse(String serverResponse) throws ResponseParsingException {
        InputStream stringInputStream = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            stringInputStream = new ByteArrayInputStream(serverResponse.getBytes());
            Document document = builder.parse(stringInputStream);
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
        finally {
            IoUtils.closeSilently(stringInputStream);
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
