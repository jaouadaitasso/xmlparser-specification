package com.alten.xmlparser

import groovy.xml.QName
import org.w3c.dom.NodeList
import org.xml.sax.DTDHandler
import org.xml.sax.EntityResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.Locator
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException
import org.xml.sax.XMLReader
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.helpers.LocatorImpl
import org.xml.sax.helpers.XMLReaderFactory
import spock.lang.Shared
import spock.lang.Specification

import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory
import java.util.stream.Collectors
import java.util.stream.Stream

class XmlParserTest extends Specification {

    InputStream inputStream

    def setup() {
        inputStream = this.getClass().getClassLoader().getResourceAsStream("documents.xml")
    }


    def "Read xml file "() {
        when: "Read xml file using XmlParser and displaying some info"
        def xmlParser = new XmlParser().parseText(convertInputStreamToString(inputStream))
        println xmlParser.'*'.size()
        println xmlParser.document.@id
        println xmlParser.document[1]

        then: "xml content should be tested"

    }

    def "isTrimWhitespace()"() {

        when: "We set up TrimWhitespace option to false"
        def xmlParser = new XmlParser(true, true)
        xmlParser.setTrimWhitespace(false)
        def realWhitespaceOption = xmlParser.isTrimWhitespace()
        def expectedTrimWhitespaceOption = false

        then: "The expectedWhitespaceOption match to the realWhitespaceOption"
        expectedTrimWhitespaceOption == realWhitespaceOption
    }

    def "isNamespaceAware()"() {

        given:
        def xmlParser = new XmlParser(true, true)

        when: "We ask if handling of namespaces is setting up successfully"
        def realNamespaceHandling = xmlParser.isNamespaceAware()
        def expectedNamespaceHandling = true

        then: "The expected and real results are matched"
        expectedNamespaceHandling == realNamespaceHandling
    }

    def "isNamespaceNotAware()"() {

        given:
        def xmlParser = new XmlParser(true, false)

        when: "We ask if handling of namespaces is setting up successfully"
        def realNamespaceHandling = xmlParser.isNamespaceAware()
        def expectedNamespaceHandling = false

        then: "The expected and real results are matched"
        expectedNamespaceHandling == realNamespaceHandling
    }


    def "isKeepIgnorableWhitespace()"() {

        when: "We ask if the keepIgnorableWhitespace option is set up successfully"
        def xmlParser = new XmlParser(true, true)
        xmlParser.setKeepIgnorableWhitespace(true)
        def realKeepIgnorableWhitespaceOption = xmlParser.isKeepIgnorableWhitespace()

        def expectedKeepIgnorableWhitespaceOption = true

        then: "The expected and real results are matched"
        expectedKeepIgnorableWhitespaceOption == realKeepIgnorableWhitespaceOption
   }


    def "setNamespaceAware()"() {

        when: "We ask if the handling of namespaces is setting up successfully"
        def xmlParser = new XmlParser(true, true)
        xmlParser.setNamespaceAware(false)
        def realNamespaceHandling = xmlParser.isNamespaceAware()
        def expectedNamespaceHandling = false

        then: "The expected and real results are matched"
        expectedNamespaceHandling == realNamespaceHandling
    }

    def "XmlParser(XMLReader xmlReader)"() {

        when: "We ask if the handling of namespaces is setting up successfully"
        def xmlParser = new XmlParser(true, true)
        xmlParser.setNamespaceAware(false)
        def realNamespaceHandling = xmlParser.isNamespaceAware()
        def expectedNamespaceHandling = false

        then: "The expected and real results are matched"
        expectedNamespaceHandling == realNamespaceHandling
    }


    def "parse(StringReader stringReader)"() {

        when: "We ask for the root element name using parse(StringReader stringReader) method"
        def realRootName = new XmlParser().parse(new StringReader(convertInputStreamToString(inputStream)))
        def expectedRootName = "documents"

        then: "The expected and real results are matched"
        expectedRootName == realRootName.name()
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {

        String newLine = System.getProperty("line.separator")
        String result = ""
        try {
            Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream)).lines()
            result = lines.collect(Collectors.joining(newLine))
        } catch(Exception e){
            println e.getMessage()
        }

        return result

    }


    def "parseText(String string)"() {

        when: "We ask for the root element name using parseText(String string) method"
        def realRootName = new XmlParser().parseText(convertInputStreamToString(inputStream))
        def expectedRootName = "documents"

        then: "The expected and real results are matched"
        expectedRootName == realRootName.name()
    }

    def "parse(File file)"() {

        given: "We convert an InputStream object to a File object"
        File file = new File("build/resources/test/NewTree.xml")
        // Java 9
        copyInputStreamToFile(inputStream, file)

        when: "We ask for the root element name using parse(File file) method"
        def realRootName = new XmlParser().parse(file)
        def expectedRootName = "documents"

        then: "The expected and real results are matched"
        expectedRootName == realRootName.name()
    }

    // Helper method to convert an InputStream object to a File object
    private static void copyInputStreamToFile(InputStream input, File file) throws IOException {
        // append = false
        try {
            OutputStream output = new FileOutputStream(file, false)
            input.transferTo(output)
        } catch(Exception e) {
            println e.getMessage()
        }

    }

    def "parse(InputSource inputSource)"() {

        given: "We convert an InputStream object to a File object"
        File file = new File("build/resources/test/NewTree.xml")
        // Java 9
        copyInputStreamToFile(inputStream, file)

        when: "We ask for the root element name using parse(InputSource inputSource) method"
        def realRootName = new XmlParser().parse(new InputSource(new FileInputStream(file)))
        def expectedRootName = "documents"

        then: "The expected and real results are matched"
        expectedRootName == realRootName.name()
    }

    def "parse(InputStream inputStream)"() {

        when: "We ask for the root element name using parse(InputSteam inputStream) method"
        def realRootName = new XmlParser().parse(inputStream)
        def expectedRootName = "documents"

        then: "The expected and real results are matched"
        expectedRootName == realRootName.name()
    }


    def "parse(String uri)"() {

        when: "We ask for the root element name using parse(String uri) method"
        def realRootName = new XmlParser().parse(this.getClass().getClassLoader().getResource("documents.xml").path)
        def expectedRootName = "documents"
        then: "The expected and real results are matched"
        expectedRootName == realRootName.name()

    }


    def "parse(XMLReader xmlReader) & getXMLReader()"() {

        when: "We create a new custom XMLReader and assign it to our XmlParser object"
        XMLReader expectedReader = XMLReaderFactory.createXMLReader()
        XmlParser realParser = new XmlParser(expectedReader)

        then: "We verify that the current reader used by the parser is the same as the one that we create"
        expectedReader == realParser.getXMLReader()

    }

    def "parse(SAXParser saxParser)"() {

        when: "We create a new custom XmlParser using the SAXParser type"
        SAXParserFactory factory = SAXParserFactory.newInstance()
        SAXParser expectedSAXParser = factory.newSAXParser()
        XmlParser realXmlParser = new XmlParser(expectedSAXParser)

        then: "We verify that the current reader used by the parser has as origin the SAXParser object that we create"
        expectedSAXParser.getXMLReader() == realXmlParser.getXMLReader()
    }


    def "parse(_), XMLReader Object is null"() {

        given: "creation of a Mock Object for XmlParser class"
        XmlParser xmlParser = Mock()

        when: "when the getXMLReader is null"
        xmlParser.getXMLReader() >> null

        then: "Any parse method with whatever parameters will be invoked"
        0 * xmlParser.parse(_)
    }


    def "setDTDHandler"() {

        when: "We set up a new DTDHandler to our XmlParser object using setDTDHandler method "
        DTDHandler expectedHandler = new DefaultHandler()
        XmlParser xmlParser = new XmlParser()
        xmlParser.setDTDHandler(expectedHandler)
        def realHandler = xmlParser.getDTDHandler()

        then: "The expectedHandler and the realHandler are matched !"
        expectedHandler == realHandler
    }

    def "setEntityResolver"() {
        given:
        def xmlParser = new XmlParser()
        // create a custom EntityResolver that ignore the DTD
        EntityResolver expectedEntityResolver = new EntityResolver() {
            @Override
            InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null
            }
        }

        when:
        // Assign this resolver to our XmlParser
        xmlParser.setEntityResolver(expectedEntityResolver)
        def realEntityResolver = xmlParser.getEntityResolver()

        then: "the two resolvers are matched !"
        expectedEntityResolver == realEntityResolver
    }

    def "setErrorHandler"() {
        given: "we create our basically object xmlParser & the customErrorHandler"
        def xmlParser = new XmlParser()
        // create a custom ErrorHandler that handle the errors occurred while parsing the document
        // (See below the CustomErrorHandler class)
        ErrorHandler expectedErrorHandler = new CustomErrorHandler()

        when: "We set up the customErrorHandler to our xmlParser"
        // Assign this error handler to our XmlParser
        xmlParser.setErrorHandler(expectedErrorHandler)
        def realErrorHandler = xmlParser.getErrorHandler()

        then: "the two errorHandlers are matched !"
        expectedErrorHandler == realErrorHandler
    }

    private class CustomErrorHandler implements ErrorHandler{
        @Override
        void warning(SAXParseException exception) throws SAXException {
            println exception.getMessage()
        }

        @Override
        void error(SAXParseException exception) throws SAXException {
            println exception.getMessage()
        }

        @Override
        void fatalError(SAXParseException exception) throws SAXException {
            println exception.getMessage()
        }
    }

    def "setFeature(String name, boolean value)"() {

        given: "We create our basically object xmlParser"
        def xmlParser = new XmlParser()
        final String FEATURE_URI = "http://xml.org/sax/features/namespaces"

        when: "We set up a new feature for our xmlParser object"
        xmlParser.setFeature(FEATURE_URI, false)
        def realFeatureValue = xmlParser.getFeature("http://xml.org/sax/features/namespaces")
        def expectedFeatureValue = false

        then: "the two feature's values are matched !"
        expectedFeatureValue == realFeatureValue
    }

    def "setProperty(String name, Object value)"() {

        given: "We create our basically xmlParser object & a custom property"
        XmlParser xmlParser = new XmlParser()
        final String PROPERTY_NAME_URI = "http://apache.org/xml/properties/input-buffer-size"


        when: "We set up the new property and its value to our object xmlParser"
        int expectedPropertyValue = 2048
        xmlParser.setProperty(PROPERTY_NAME_URI, new Integer(expectedPropertyValue))
        int realPropertyValue = (int) xmlParser.getProperty(PROPERTY_NAME_URI)

        then: "the two values for the properties are matched !"
        expectedPropertyValue == realPropertyValue
    }


    def "addTextToNode() trimWhitespace => false, keepIgnorableWhitespace => true"() {
        given: "Initialization of mocked NodeList and Node objects, and also the real object XmlParser"
        groovy.util.NodeList list = Mock(groovy.util.NodeList)
        // Initialization of a mocked node
        Node parent= Mock(Node)
        // Initialization of a real XmlParser object
        XmlParser xmlParser = new XmlParser()
        // setting up the node parent of the XmlParser object
        xmlParser.setParent(parent)
        parent.children() >> list

        when: "When calling the addTextToNode method"
        xmlParser.setTrimWhitespace(false)
        xmlParser.setKeepIgnorableWhitespace(true)
        xmlParser.addTextToNode()

        then: "the parent.children() that return the 'list' object that we just create should be invoked," +
              "therefore, it should invoke also its add() method"
        1 * list.add(_ as String)
    }





    def "addTextToNode() trimWhitespace => false, text.trim().length > 0"() {
        given: "Initialization of mocked NodeList and Node objects, and also the real object XmlParser"
        groovy.util.NodeList list = Mock(groovy.util.NodeList)
        // Initialization of a mocked node
        Node parent= Mock(Node)
        StringBuilder bodyText = new StringBuilder("This chain of characters has a length > 0")
        // Initialization of a real XmlParser object
        XmlParser xmlParser = new XmlParser()
        // setting up the node parent and the bodyText of the XmlParser object
        xmlParser.setParent(parent)
        xmlParser.setBodyText(bodyText)
        parent.children() >> list

        when: "When calling the addTextToNode method"
        xmlParser.setTrimWhitespace(false)
        xmlParser.addTextToNode()

        then: "the parent.children() that return the 'list' object that we just create should be invoked," +
                "therefore, it should invoke also its add() method"
        1 * list.add(_ as String)
    }

    def "addTextToNode() text.trim().length > 0"() {
        given: "Initialization of mocked NodeList and Node objects, and also the real object XmlParser"
        groovy.util.NodeList list = Mock(groovy.util.NodeList)
        // Initialization of a mocked node
        Node parent= Mock(Node)
        StringBuilder bodyText = new StringBuilder("This chain of characters has a length > 0")
        // Initialization of a real XmlParser object
        XmlParser xmlParser = new XmlParser()
        // setting up the node parent and the bodyText of the XmlParser object
        xmlParser.setParent(parent)
        xmlParser.setBodyText(bodyText)
        parent.children() >> list

        when: "When calling the addTextToNode method"
        xmlParser.setTrimWhitespace(true)
        xmlParser.addTextToNode()

        then: "the parent.children() that return the 'list' object that we just create should be invoked," +
                "therefore, it should invoke also its add() method"
        1 * list.add(_ as String)
    }


    def "getElementName(String namespaceURI, String localName, String qName) => localName == null"() {
        given:
        XmlParser xmlParser = new XmlParser()

        expect:
        new QName("https://...", "foo:", "foo") ==
                xmlParser.getElementName("https://...", null, "foo:")
    }


    def "getDocumentLocator()"() {
        given:

        Locator locator = new LocatorImpl()
        XmlParser xmlParser = new XmlParser()

        when: "Setting up the current locator to our usual object XmlParser"
        xmlParser.setDocumentLocator(locator)

        then:
        locator == xmlParser.getDocumentLocator()
    }

    def "getParent()"() {
        given:

        Node parent = new Node(null, "newElement", "Some text goes here !")
        XmlParser xmlParser = new XmlParser()

        when: "Setting up the current parent to our usual object XmlParser"
        xmlParser.setParent(parent)

        then:
        parent == xmlParser.getParent()
    }

    def "getBodyText()"() {
        given:

        StringBuilder bodyText = new StringBuilder()
        XmlParser xmlParser = new XmlParser()

        when: "Setting up the current parent to our usual object XmlParser"
        xmlParser.setBodyText(bodyText)

        then:
        bodyText == xmlParser.getBodyText()
    }

}
