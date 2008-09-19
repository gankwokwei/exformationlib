package exformation.net;

import java.io.CharArrayWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLPacketParser extends DefaultHandler{
	
	public XMLPacket packet = new XMLPacket();
	
	protected CharArrayWriter contents = new CharArrayWriter();
	
	/**
	 * Called every time we parse the char buffer
	 */	
	public void characters( char[] ch, int start, int length ) throws SAXException {
		contents.write( ch, start, length );
	}
	
	public String nodeContent(){
		return contents.toString();
	}
	
	public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) throws SAXException {
		contents.reset();
	}
	
	public void endElement( String namespaceURI, String localName, String qName ) throws SAXException{
		elementParsed(localName);
	}
	
	public void elementParsed(String localName){
		//packet.xmlString;
	}
}
