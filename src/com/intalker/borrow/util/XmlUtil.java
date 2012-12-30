package com.intalker.borrow.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import com.intalker.borrow.data.BookInfo;

import android.database.CursorJoiner.Result;
import android.util.Xml;

public class XmlUtil {
	private static final String Encoding = "utf-8";
	private static final String BooksNodeName = "books";
	private static final String BookNodeName = "book";
	private static final String ISBNAttributeName = "isbn";
//	public static void save(ArrayList<BookInfo> bookInfoList, OutputStream out)
//			throws Exception {
//		XmlSerializer serializer = Xml.newSerializer();
//		serializer.setOutput(out, Encoding);
//		serializer.startDocument(Encoding, true);
//		serializer.startTag(null, BooksNodeName);
//		for (BookInfo bookInfo : bookInfoList) {
//			serializer.startTag(null, BookNodeName);
//			serializer.attribute(null, ISBNAttributeName, bookInfo.getISBN());
//
////			serializer.startTag(null, "name");
////			serializer.text(bookInfo.getISBN());
////			serializer.endTag(null, "name");
//
//			serializer.endTag(null, BookNodeName);
//		}
//
//		serializer.endTag(null, BooksNodeName);
//		serializer.endDocument();
//		out.flush();
//		out.close();
//	}
	
	public static ArrayList<BookInfo> parseCachedBooks() {
		ArrayList<BookInfo> books = new ArrayList<BookInfo>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File cachedXmlFilePath = new File(StorageUtil.CacheXmlPath
					+ "/test.txt");
			// builder.parse(cachedXmlFilePath)
			Document doc = builder.parse(cachedXmlFilePath);
			Element rootElement = doc.getDocumentElement();
			NodeList items = rootElement.getElementsByTagName(BookNodeName);
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				NamedNodeMap attributes = item.getAttributes();
				// NodeList properties = item.getChildNodes();

				Node isbnNode = attributes.getNamedItem(ISBNAttributeName);
				String isbn = isbnNode.getNodeValue();

				BookInfo book = new BookInfo(isbn);

				books.add(book);
			}
		} catch (Exception ex) {
		}
		return books;
	}
	
	public static String serializeCachedBooks(ArrayList<BookInfo> books) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		
		Element rootElement = doc.createElement(BooksNodeName);

		for (BookInfo book : books) {
			Element bookElement = doc.createElement(BookNodeName);
			bookElement.setAttribute(ISBNAttributeName, book.getISBN());
			
//			Element nameElement = doc.createElement("name");
//			nameElement.setTextContent(book.getName());
//			bookElement.appendChild(nameElement);
//			
//			Element priceElement = doc.createElement("price");
//			priceElement.setTextContent(book.getPrice() + "");
//			bookElement.appendChild(priceElement);
			
			rootElement.appendChild(bookElement);
		}
		
		doc.appendChild(rootElement);
        
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        
        StringWriter writer = new StringWriter();
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        
		return writer.toString();
	}
}
