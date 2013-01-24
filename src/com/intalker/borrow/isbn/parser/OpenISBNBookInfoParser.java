package com.intalker.borrow.isbn.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.intalker.borrow.util.WebUtil;

public class OpenISBNBookInfoParser extends BookInfoParser {
	private final static String ISBN_SEARCHURL_OPENISBN = "http://openisbn.com/isbn/";

	public OpenISBNBookInfoParser() {
	}

	@Override
	public void parse() {
		String strURL = ISBN_SEARCHURL_OPENISBN + mISBN;
		try {
			Document doc = Jsoup.connect(strURL).get();

			// Book name.
			Elements elements = doc.getElementsByClass("PostHead");
			if (elements.size() > 0) {
				Element bookNameElement = elements.get(0);
				if (bookNameElement.children().size() > 0) {
					mBookName = bookNameElement.child(0).ownText();
				}
			}

			elements = doc.getElementsByClass("Article");
			if (elements.size() > 0) {
				Element articleElement = elements.get(0);
				
				Elements subElements = null;
				
				if (null == mCoverImage) {
					subElements = articleElement.getElementsByTag("img");
					if (subElements.size() > 0) {
						Element imageElement = subElements.get(0);
						String imageURL = imageElement.attr("src");
						mCoverImage = WebUtil.getImageFromURL(imageURL);
					}
				}
				
				subElements = articleElement
						.getElementsByClass("PostContent");

				if (subElements.size() > 0) {
					String sAll = subElements.get(0).ownText();

					int startIndexOfAuthor = 8;
					int endIndexOfAuthor = sAll.indexOf("Publisher:") - 1;
					mAuthor = sAll.substring(startIndexOfAuthor,
							endIndexOfAuthor);

					int startIndexOfPublisher = endIndexOfAuthor + 12;
					int endIndexOfPublisher = sAll.indexOf("Pages:") - 1;
					mPublisher = sAll.substring(startIndexOfPublisher,
							endIndexOfPublisher);

					int startIndexOfPageCount = endIndexOfPublisher + 8;
					int endIndexOfPageCount = sAll.indexOf("Published:") - 1;
					mPageCount = sAll.substring(startIndexOfPageCount,
							endIndexOfPageCount);
				}
			}

			elements = doc.getElementsByClass("div");
			if (elements.size() > 0) {
				Element descriptionElement = elements.get(0);
				mDescription = descriptionElement.ownText();
			}

			//
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// mCoverImage = WebUtil.getImageFromURL(imageURL);
	}
}
