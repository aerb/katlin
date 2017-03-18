package com.yourdomain.katlin

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.Reader
import javax.xml.parsers.DocumentBuilderFactory

fun Reader.readXml(): Document {
    return DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .parse(InputSource(this))
}

fun NodeList.asSequence(): Sequence<Node> {
    var i = 0
    return generateSequence {
        if(i >= this.length) null
        else this.item(i++)
    }
}