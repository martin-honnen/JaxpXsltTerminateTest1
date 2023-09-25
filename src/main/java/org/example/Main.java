package org.example;

import net.sf.saxon.jaxp.TransformerImpl;
import net.sf.saxon.s9api.MessageListener2;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Main {
    boolean terminated = false;
    String terminationMessage;

    static TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public static void main(String[] args) {
        Main main = new Main();
        main.transform("sample2.xml", "sheet1.xsl");
        System.out.println();
        main = new Main();
        main.transform("sample1.xml", "sheet1.xsl");

    }

    public void transform(String xmlSource, String xsltSource) {
        try {
            Transformer xsltProcessor = transformerFactory.newTransformer(new StreamSource(xsltSource));

            ((TransformerImpl)xsltProcessor).getUnderlyingXsltTransformer().setMessageHandler(message -> {
                if (message.isTerminate()) {
                    terminated = true;
                    terminationMessage = message.getStringValue();
                }
            });

            xsltProcessor.transform(new StreamSource(xmlSource), new StreamResult(System.out));

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            System.out.println(e.getMessageAndLocation());
            if (terminated)
                System.out.println(terminationMessage);
        }
    }
}