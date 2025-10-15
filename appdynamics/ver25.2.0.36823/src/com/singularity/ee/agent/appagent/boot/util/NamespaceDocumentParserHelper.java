/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.util;

import com.singularity.ee.agent.appagent.boot.util.DocumentParseHelper;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.namespace.NamespaceContext;

public class NamespaceDocumentParserHelper
extends DocumentParseHelper {
    private final Map<String, String> prefixToUri = new ConcurrentHashMap<String, String>();

    public NamespaceDocumentParserHelper() {
        this.xPathInstance.setNamespaceContext(new NamespaceContext(){

            @Override
            public String getNamespaceURI(String prefix) {
                return (String)NamespaceDocumentParserHelper.this.prefixToUri.get(prefix);
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            public Iterator getPrefixes(String namespaceURI) {
                return null;
            }
        });
    }

    public void updateNamespaceURI(Map<String, String> prefixToUri) {
        this.prefixToUri.clear();
        this.prefixToUri.putAll(prefixToUri);
    }
}

