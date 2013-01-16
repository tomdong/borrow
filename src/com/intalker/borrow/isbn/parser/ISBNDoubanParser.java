package com.intalker.borrow.isbn.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.intalker.borrow.util.WebUtil;

public class ISBNDoubanParser extends ISBNParserBase {
	private final static String ISBN_SEARCHURL_DOUBAN = "http://api.douban.com/book/subject/isbn/";
	
	public ISBNDoubanParser(String isbn) {
		super(isbn);
	}

	@Override
	public void parse() {
		String url = ISBN_SEARCHURL_DOUBAN + mISBN;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String imageURL = "";
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(url);
			
			//Get preview image (cover image)
			NodeList nodes = doc.getElementsByTagName("link");
			int length = nodes.getLength();
			for (int i = 0; i < length; ++i) {
				Node node = nodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				Node relAttr = attrs.getNamedItem("rel");
				if (relAttr.getNodeValue().compareTo("image") == 0) {
					imageURL = attrs.getNamedItem("href").getNodeValue();
					imageURL = imageURL.replace("spic", "lpic");
					break;
				}
			}
			
			//Get title text
			nodes = doc.getElementsByTagName("title");
			if(nodes.getLength() > 0)
			{
				Node summaryNode = nodes.item(0);
				mBookName = summaryNode.getTextContent();
				//mBookName = summaryNode.getNodeValue();
			}
			
			//Get summary text
			nodes = doc.getElementsByTagName("summary");
			if(nodes.getLength() > 0)
			{
				Node summaryNode = nodes.item(0);
				mDescription = summaryNode.getTextContent();
			}
			
			nodes = doc.getElementsByTagName("db:attribute");
			length = nodes.getLength();
			for (int i = 0; i < length; ++i) {
				Node node = nodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				Node relAttr = attrs.getNamedItem("name");
				
				if (relAttr.getNodeValue().compareTo("isbn13") == 0) {
					mISBN = node.getTextContent();
				}
				else if (relAttr.getNodeValue().compareTo("pages") == 0) {
					mPageCount = node.getTextContent();
				}
				else if (relAttr.getNodeValue().compareTo("author") == 0) {
					mAuthor = node.getTextContent();
				}
				else if (relAttr.getNodeValue().compareTo("publisher") == 0) {
					mPublisher = node.getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		mCoverImage = WebUtil.getImageFromURL(imageURL);
	}
}
