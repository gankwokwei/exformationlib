package exformation.net;

	
	import java.io.IOException;
	/**
	 * XMLString is a root node name and XML string of the root node,
	 * Simply the root node name  and the string
	 * Used by XMLSocket
	 * */
	class XMLString {
		/**
		 * The string associated with the XMLString (this is the XML)
		 * */
		String string;
		/**
		 * The name of the XMLString parent node
		 * */
		String name = null;
		/**
		 * The XMLString Constructor
		 * @param str the XML String to initialize with and parse the 
		 * parentnode
		 * @throws IOException for a bad string
		 * */
		public XMLString(String str) throws IOException {
			if (str == null || str.length() < 1) {
				throw new IOException("String is not valid XML!");
			}
			if (str.charAt(0) !='<') {
				name = "";
				string = str;
			} else {
				for (int i = 1; i < str.length(); i++) {
					if (str.charAt(i)=='/' || str.charAt(i) == '>') {
						name = str.substring(1,i);
						break;
					}
				}
				if (name == null) { name = ""; }
				string = str;
			}
		}
		/**
		 * The XMLString Constructor
		 * @param name the XMLString name
		 * @param str the XMLString xml
		 * */
		public XMLString(String name,String str) { 
			this.name = name;
			this.string = str;
		}
		/**
		 * @return Name of the XMLString (parentTag)
		 * */
		public String getName() {
			return name;
		}
		/**
		 * @return XML of the XMLString
		 * */
		public String getXML() {
			return string;
		}
		/**
		 * @return XML of the XMLString
		 * */
		public String toString() { return string; } 
	}


