package exformation.net;


import java.net.*;
import java.beans.*;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import exformation.core.BaseClass;


/**
 * XMLSocket is a socket wrapper which reads the socket to get XML blocks.
 * Messages are split up and sent to the listeners.
 * XMLSocket is used to read and parse XML from a socket.
 * */
class XMLClient extends BaseClass implements Runnable {
	
	public static final String NEW_MESSAGE ="newMessage";
	public static final String DISCONNECT  ="disconnect";
	
	/**
	 * This is the original socket
	 * */
	private Socket client = null;
	/**
	 * This is the input stream from the socket
	 * */
	private InputStream is;
	/**
	 * This is the reader used to read the inputstream from the socket
	 * */
	private BufferedReader br;
	/**
	 * This is the outputstream from the socket
	 * */
	private OutputStream os;
	/**
	 * This is the Writer used to write the outputstream from the socket
	 * */
	private PrintWriter pw;
	/**
	 * This is a Queue of Incoming Messages
	 * */
	private SyncList incoming = new SyncList();
	/**
	 * Flag indicates if the socket is done reading and writing (forever)
	 * */
	private boolean done = false;
	/**
	 * Does the system use fireEvents to notify listeners
	 * */
	private boolean throwit = true;
	/**
	 * The thread used for reading the connection.
	 * */
	private Thread thread = null;
	/**
	 * Default constructor
	 * @param client the ClientHandler
	 * */
	public XMLClient(String url, int port){
		try{
			this.client = new Socket(url,port);
			os = client.getOutputStream();
			is = client.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(os,true);
			
		}catch(Throwable any){
			debug(any);
		}
	}
	/**
	 * Starts the Reader Thread
	 * */
	public void startThread() {
		if (thread==null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	/**
	 * tell the XMLSocket to fire newMessage events per each successful
	 * read or not.
	 * @param t "Fire a newMessage event per xml event?"
	 * */
	public void setThrowEvent(boolean t) {
		throwit = t;
	}
	/**
	 * reader thread, fires events
	 * */
	public void run() {
		while (!done) {
			try {
				String xml = getXML();
				//System.err.println("XML Socket Recv'd:"+xml);
				if (xml.length() <= 0) {
					throw new IOException("Server Closed!");
				}
				XMLString xmlStr = new XMLString(xml);
				incoming.add(xmlStr);
				if (throwit) {
					dispatchEvent(NEW_MESSAGE, xmlStr);
				}
			} catch (IOException e) {
				System.err.println("Closing Socket on Message: ["+e.getMessage()+"]");
				done = true;
			}
		}
		dispatchEvent(DISCONNECT, new XMLString("disconnect","<disconnect></disconnect>"));
	}
	
	private void dispatchEvent(String type,XMLString data){
		pcs.firePropertyChange(type, null, data);
	}
	/**
	 * Is the XMLSocket finished with the socket
	 * @return boolean true if finished false otherwise
	 * */
	public boolean isDone() { return done; }
	/**
	 * sends an string to the socket (preferrably XML)
	 * @param message  the message to send.
	 * */
	public void sendMessage(String message) {
		pw.println(message);
	}
	/**
	 * is there a message waiting in the queue
	 * @return boolean true if a message is on queue false otherwise
	 * */
	public boolean hasMessage() {
		return (!incoming.isEmpty());
	}
	/**
	 * get the next message from the queue
	 * @return XMLString the next message on the queue
	 * */
	public XMLString getNextMessage() {
		return (XMLString)(incoming.removeFirst());
	}
	/**
	 * Put a message at the start of the queue
	 * @param xml the XMLString message to put at the start of the queue
	 * */
	public void unPopMessage(XMLString xml) {
		incoming.addFirst(xml);
	}
	/**
	 * Parse XML with a parser, results are in the XParser
	 * @param up the XParser to the parse the XML
	 * @param read the reader to provide the XML.
	 * @throws SAXException if there is an XML parser error
	 * @throws IOException if there is an IO parser error
	 * */
	public void parseXML(UPacketParser up,Reader read) throws SAXException,IOException {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler( up );
		xr.parse(new InputSource(read));
	}
	/**
	 * Parse XML with a parser, results are in the XParser
	 * @param up the XParser to the parse the XML
	 * @param xml the XML to parse.
	 * @throws SAXException if there is an XML parser error
	 * @throws IOException if there is an IO parser error
	 * */
	public void parseXML(XMLPacketParser up,String xml) throws SAXException,IOException {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler( up );
		xr.parse(new InputSource(new StringReader(xml)));
	}
	/**
	 * Get XML from the stream which has the tag name..
	 * @param parentTag the root tag name
	 * @return String xml message
	 * @throws IOException in case of IO Error or Parse Error
	 * */
	public String getXML(String parentTag) throws IOException { //throw an exception
		String parent = "<"+parentTag+">";
		String endParent = "</"+parentTag+">";
		boolean par = true;
		boolean body = false;
		boolean done = false;
		StringBuffer xml = new StringBuffer("");
		int parentIndex = 0;
		//int count = 0;
		while (!done) {
			int ch = br.read();
			if (ch==-1) {
				done = true;
			}
			if (body) {
				if (ch == endParent.charAt(parentIndex) && parentIndex <  endParent.length()) {
					parentIndex++;
					if (parentIndex == endParent.length()) {
						done = true;
					}
				} else  {
					parentIndex = 0;
				}
				xml.append((char)ch);
			} else if (par == true) { 	//we can use a simple one because
						//of < > in XML
				if (parentIndex < parent.length() && ch == parent.charAt(parentIndex)) {
					parentIndex++;
					if (parentIndex == parent.length()) {
						xml.append(parent);
						body = true;
						par = false;
						parentIndex = 0;
					}
				} else {
					return null;
				}
			}
		}
		return xml.toString();
	}
	/**
	 * get the XML message from the socket 
	 * @throws IOException in case of IO Error or Parse Error
	 * */
	private String getXML() throws IOException { //throw an exception
		String parent = "";
		String parentS = "<";
		String parentE =  ">";
		String parentTag = "";
		String endParent = "";
		String endParentS = "</";
		String endParentE = ">";
		boolean par = true;
		boolean body = false;
		boolean done = false;
		StringBuffer xml = new StringBuffer("");
		int parentIndex = 0;
		//int count = 0;
		while (!done) {
			int chi = br.read();
			if (chi==-1) {
				done = true;
			}
			char ch = (char)chi;
			if (body) {
				if (ch == endParent.charAt(parentIndex) && parentIndex <  endParent.length()) {
					parentIndex++;
					if (parentIndex == endParent.length()) {
						done = true;
					}
				} else  {
					parentIndex = 0;
				}
				xml.append((char)ch);
			} else if (par == true) { 	//we can use a simple one because
						//of < > in XML
				if (parentIndex == 0) {
					if (ch == parentS.charAt(0)) {
						parentIndex++;
						parent = ""+ch;
					} else {
						parent = "";
						parentIndex = 0;
					}
				} else { //parent body
					if (ch == parentE.charAt(0)) {
						parent = parent + ch;
						parentTag = parent.substring(1,parent.length()-1);
						body = true;
						par = false;
						parentIndex = 0;
						endParent = endParentS + parentTag + endParentE;
						xml.append(parent);
					} else {
						parent = parent + ch;
						parentIndex++;
					}
				}
			}
		}
		return xml.toString();
	}
	/**
	 * the propertyChangeSupport to fire off events.
	 * */
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**
	 * adds a PropertyChangeListener for a message
	 * @param pcl the property change listener to add
	 * */
	public void addPropertyChangeListener( PropertyChangeListener pcl) { 
		pcs.addPropertyChangeListener(pcl);
	}
	/**
	 * adds a PropertyChangeListener for a message
	 * @param propertyName the property to watch for
	 * @param pcl the property change listener to add
	 * */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(propertyName,pcl);
	}
	/**
	 * closes the socket
	 * @throws IOException in case of IO Error
	 * */
	public synchronized void close() throws IOException {
		done = true;
		client.close();
	}
}
