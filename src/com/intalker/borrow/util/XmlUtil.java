package com.intalker.borrow.util;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.intalker.borrow.data.BookInfo;

public class XmlUtil {
	private static final String Encoding = "utf-8";
	private static final String BooksNodeName = "books";
	private static final String BookNodeName = "book";
	private static final String ISBNAttributeName = "isbn";
	private static final String BookNameNodeName = "name";
	private static final String AuthorNodeName = "author";
	private static final String PublisherNodeName = "publisher";
	private static final String PageCountNodeName = "pagecount";
	private static final String DescriptionNodeName = "description";
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
			File cachedXmlFilePath = new File(StorageUtil.CacheBookIndexPath);
			Document doc = builder.parse(cachedXmlFilePath);
			Element rootElement = doc.getDocumentElement();
			NodeList items = rootElement.getElementsByTagName(BookNodeName);
			int nodeCount = items.getLength();
			for (int i = 0; i < nodeCount; ++i) {
				Node item = items.item(i);
				if(null == item)
				{
					continue;
				}
				NamedNodeMap attributes = item.getAttributes();
				// NodeList properties = item.getChildNodes();

				Node isbnNode = attributes.getNamedItem(ISBNAttributeName);
				String isbn = isbnNode.getNodeValue();
				
				BookInfo book = new BookInfo(isbn);

				NodeList subNodes = item.getChildNodes();
				int subNodeCount = subNodes.getLength();
				for(int j = 0; j < subNodeCount; ++j)
				{
					Node subNode = subNodes.item(j);
					if(null == subNode)
					{
						continue;
					}
					
					String nodeTagName = subNode.getNodeName();
					if (nodeTagName.compareTo(BookNameNodeName) == 0) {
						String bookName = subNode.getTextContent();
						book.setName(bookName);
					}
					
					String authorTagName = subNode.getNodeName();
					if (authorTagName.compareTo(AuthorNodeName) == 0) {
						String author = subNode.getTextContent();
						book.setAuthor(author);
					}
					
					String publisherTagName = subNode.getNodeName();
					if (publisherTagName.compareTo(PublisherNodeName) == 0) {
						String publisher = subNode.getTextContent();
						book.setPublisher(publisher);
					}
					
					String pageCountTagName = subNode.getNodeName();
					if (pageCountTagName.compareTo(PageCountNodeName) == 0) {
						String pageCount = subNode.getTextContent();
						book.setPageCount(pageCount);
					}
					
					String descriptionTagName = subNode.getNodeName();
					if (descriptionTagName.compareTo(DescriptionNodeName) == 0) {
						String description = subNode.getTextContent();
						book.setDescription(description);
					}
				}
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

			Element nameElement = doc.createElement(BookNameNodeName);
			nameElement.setTextContent(book.getName());
			bookElement.appendChild(nameElement);
			
			Element authorElement = doc.createElement(AuthorNodeName);
			authorElement.setTextContent(book.getAuthor());
			bookElement.appendChild(authorElement);
			
			Element publisherElement = doc.createElement(PublisherNodeName);
			publisherElement.setTextContent(book.getPublisher());
			bookElement.appendChild(publisherElement);
			
			Element pageCountElement = doc.createElement(PageCountNodeName);
			pageCountElement.setTextContent(book.getPageCount());
			bookElement.appendChild(pageCountElement);
			
			Element descriptionElement = doc.createElement(DescriptionNodeName);
			descriptionElement.setTextContent(book.getDescription());
			bookElement.appendChild(descriptionElement);
			
			rootElement.appendChild(bookElement);
		}
		
		doc.appendChild(rootElement);
        
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, Encoding);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        
        StringWriter writer = new StringWriter();
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        
		return writer.toString();
	}
}
