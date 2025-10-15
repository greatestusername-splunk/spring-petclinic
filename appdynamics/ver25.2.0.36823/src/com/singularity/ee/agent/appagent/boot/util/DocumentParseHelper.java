/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.util;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DocumentParseHelper {
    final XPath xPathInstance;
    private boolean invalidDocumentClassMessageLogged = false;

    public DocumentParseHelper() {
        XPathFactory factory = XPathFactory.newInstance();
        this.xPathInstance = factory.newXPath();
    }

    public String getNameFromXPathList(Object document, List<String> xPathExpressions, List<Boolean> xPathOptional, String initialName, IAgentBootLogger logger) throws Exception {
        if (!(document instanceof Document)) {
            if (!this.invalidDocumentClassMessageLogged) {
                logger.warn("Application using unsupported type of [" + document.getClass().getName() + "]. Could not resolve transaction name.");
                this.invalidDocumentClassMessageLogged = true;
            }
            return null;
        }
        this.invalidDocumentClassMessageLogged = false;
        Document doc = (Document)document;
        logger.debugParams("Document parsed {}", this.safeGetStringRepresentationForElement(doc.getDocumentElement(), logger));
        StringBuilder sb = new StringBuilder();
        sb.append(initialName);
        for (int i = 0; i < xPathExpressions.size(); ++i) {
            logger.debugParams("compiling xpath expression [{}]", xPathExpressions.get(i));
            XPathExpression expr = this.xPathInstance.compile(xPathExpressions.get(i));
            String result = expr.evaluate(doc.getDocumentElement());
            logger.debugParams("result after evaluation [{}]", result);
            if (result != null && !"".equals(result)) {
                sb.append(".");
                sb.append(result);
                continue;
            }
            if (xPathOptional.get(i).booleanValue()) continue;
            logger.debugParams("could not find result for xpath expression [{}]", expr);
            return null;
        }
        if (!initialName.equals(sb.toString())) {
            logger.debugParams("initial name: {} derived name: {}", initialName, sb.toString());
            return sb.toString();
        }
        return null;
    }

    public String safeGetStringRepresentationForElement(Object element, IAgentBootLogger logger) {
        String xmlAsString = "";
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(new DOMSource((Element)element), new StreamResult(new OutputStreamWriter((OutputStream)stream, "UTF-8")));
            xmlAsString = new String(stream.toByteArray(), "UTF-8");
        }
        catch (TransformerConfigurationException e) {
            logger.debugParams("Error: {}", e.getStackTrace());
        }
        catch (TransformerException e) {
            logger.debugParams("Error: {}", e.getStackTrace());
        }
        catch (UnsupportedEncodingException e) {
            logger.debugParams("Error: {}", e.getStackTrace());
        }
        return xmlAsString;
    }
}

