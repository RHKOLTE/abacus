package org.tux.bagleyd.abacus.learn;

/*
 * @(#)AbacusExamples.java
 *
 * Copyright 2015  David A. Bagley, bagleyd@tux.org
 *
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose and without fee is hereby granted,
 * provided that the above copyright notice appear in all copies and
 * that both that copyright notice and this permission notice appear in
 * supporting documentation, and that the name of the author not be
 * used in advertising or publicity pertaining to distribution of the
 * software without specific, written prior permission.
 *
 * This program is distributed in the hope that it will be "useful",
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.tux.bagleyd.abacus.AbacusInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The <code>AbacusExamples</code> class handles the examples for Abacus.
 *
 * @author	David A. Bagley
 * @author	bagleyd@tux.org
 * @author	http:/www.tux.org/~bagleyd/life.html
 */

public class AbacusExamples {
	static final boolean debug = false;

	private static String libraryText[][][][][][] = null;
	private static String libraryChapters[][] = null;

	public static void setLibrary(String fileName, URL url) {
		libraryText = readExamplesXML(fileName, url);
	}

	static String[][][][][][] readExamplesXML(String fileName, URL url) {
		try {
			DocumentBuilderFactory docBuilderFactory =
				DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder =
				docBuilderFactory.newDocumentBuilder();
			Document doc;

			if (url == null) {
				doc = docBuilder.parse(new File(fileName));
			} else {
				doc = docBuilder.parse(new URL(url, fileName).openStream());
				//System.out.println("Loaded xml: " + url + fileName);
			}

			// normalize text representation
			doc.getDocumentElement().normalize();
			/*if (debug)
				System.out.println("Root element of the doc is " +
					 doc.getDocumentElement().getNodeName());*/
			NodeList listOfBooks = doc.getElementsByTagName("book");
			int totalBooks = listOfBooks.getLength();

			if (debug)
				System.out.println("Number of books: " + totalBooks);

			if (libraryChapters == null) {
				libraryChapters = new String[totalBooks][];
			}
			String[][][][][][] booksText = new String[totalBooks][][][][][];
			for (int i = 0; i < totalBooks; i++) {
				Node bookNode = listOfBooks.item(i);
				if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
					booksText[i] = readBook(bookNode, i);
				}
			}
			return booksText;
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line " +
				err.getLineNumber() + ", uri " +
				err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static String[][][][][] readBook(Node bookNode, int book) {
		try {
			Element bookElement = (Element) bookNode;
			String name, author;
			Node namedItem = bookElement.getAttributes().getNamedItem("name");
			name = (namedItem == null) ? "" : namedItem.getNodeValue();
			Node authorItem = bookElement.getAttributes().getNamedItem("author");
			author = (authorItem == null) ? "" : authorItem.getNodeValue();
			NodeList listOfEditions = bookElement.getElementsByTagName("edition");
			int totalEditions = listOfEditions.getLength();

			if (debug)
				System.out.println("Book name: " + name
					+ ", author: " + author
					+ ", number of editions: " + totalEditions);

			String[][][][][] editionsText = new String[totalEditions][][][][];
			for (int i = 0; i < totalEditions; i++) {
				Node editionNode = listOfEditions.item(i);
				if (editionNode.getNodeType() == Node.ELEMENT_NODE) {
					editionsText[i] = readEdition(editionNode, book);
				}
			}
			return editionsText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static String[][][][] readEdition(Node editionNode, int book) {
		try {
			Element editionElement = (Element) editionNode;
			String version;
			Node versionItem = editionElement.getAttributes().getNamedItem("version");
			version = (versionItem == null) ? "" : versionItem.getNodeValue();
			NodeList listOfChapters = editionElement.getElementsByTagName("chapter");
			int totalChapters = listOfChapters.getLength();
			if (libraryChapters[book] == null) {
				libraryChapters[book] = new String[totalChapters];
			} else if (libraryChapters[book].length != totalChapters) {
				System.out.println("Old number of chapters " + libraryChapters[book].length
					+ " does not match " + totalChapters);
			}

			if (debug)
				System.out.println("Edition version: " + version
					+ ", number of chapters: " + totalChapters);

			String[][][][] chaptersText = new String[totalChapters][][][];
			for (int i = 0; i < totalChapters; i++) {
				Node chapterNode = listOfChapters.item(i);
				if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
					chaptersText[i] = readChapter(chapterNode, book, i);
				}
			}
			return chaptersText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static String[][][] readChapter(Node chapterNode, int book, int index) {
		try {
			Element chapterElement = (Element) chapterNode;
			String name;
			Node namedItem = chapterElement.getAttributes().getNamedItem("name");
			name = (namedItem == null) ? "" : namedItem.getNodeValue();
			if (libraryChapters[book][index] == null) {
				libraryChapters[book][index] = new String(name);
			} else if (!libraryChapters[book][index].equals(name)) {
				System.out.println("Old chapter name " + libraryChapters[book][index]
					+ " does not match " + name);
			}
			NodeList listOfLessons = chapterElement.getElementsByTagName("lesson");
			int totalLessons = listOfLessons.getLength();

			if (debug)
				System.out.println("Chapter name: " + name
					+ ", number of lessons: " + totalLessons);

			String[][][] lessonsText = new String[totalLessons][][];
			for (int i = 0; i < totalLessons; i++) {
				Node lessonNode = listOfLessons.item(i);
				if (lessonNode.getNodeType() == Node.ELEMENT_NODE) {
					lessonsText[i] = readLesson(lessonNode);
				}
			}
			return lessonsText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static String[][] readLesson(Node lessonNode) {
		try {
			Element lessonElement = (Element) lessonNode;
			String name;
			Node namedItem = lessonElement.getAttributes().getNamedItem("name");
			name = (namedItem == null) ? "" : namedItem.getNodeValue();
			NodeList listOfMoves = lessonElement.getElementsByTagName("move");
			int totalMoves = listOfMoves.getLength();

			if (debug)
				System.out.println("Lesson name: " + name
					+ ", number of moves: " + totalMoves);

			String[][] movesText = new String[totalMoves][];
			for (int i = 0; i < totalMoves; i++) {
				Node moveNode = listOfMoves.item(i);
				if (moveNode.getNodeType() == Node.ELEMENT_NODE) {
					movesText[i] = readMove(moveNode);
				}
			}
			return movesText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static String[] readMove(Node moveNode) {
		try {
			Element moveElement = (Element) moveNode;

			String code = "";
			NodeList codeList = moveElement.getElementsByTagName("code");
			Element codeElement = (Element)codeList.item(0);
			NodeList textCodeList = codeElement.getChildNodes();
			if ((textCodeList.item(0)) != null)
				code = (textCodeList.item(0)).getNodeValue().trim();
			String[] codes = null;
			int codeSize = 0;
			if (code != null) {
				codes = code.split(" ");
				codeSize = codes.length;
			}
			int[] codePart = new int[codeSize];
			for (int i = 0; i < codeSize; i++) {
				if (codes == null) {
					codePart[i] = -1;
				} else {
					try {
						codePart[i] = Integer.parseInt(codes[i]);
					} catch (Exception e) {
						codePart[i] = -1;
					}
				}
			}

			String text = "";
			NodeList textList = moveElement.getElementsByTagName("text");
			Element textElement = (Element)textList.item(0);
			NodeList textTextList = textElement.getChildNodes();
			if ((textTextList.item(0)) != null)
				text = (textTextList.item(0)).getNodeValue().trim();
			String texts[] = null;
			int textSize = 0;
			if (text != null) {
				texts = text.split("\n");
				textSize = texts.length;
			}
			if (texts != null) {
				for (int i = 0; i < textSize; i++) {
					texts[i] = texts[i].trim();
				}
			}

			if (debug) {
				System.out.println("Move code (" + codeSize + "): " + toString(codePart) );
				System.out.println("Move text (" + textSize + "): " + toString(texts));
			}
			String[] moveText = new String[codeSize + 1];
			moveText[0] = code;
			if (texts != null) {
				for (int i = 0; i < textSize; i++) {
					moveText[i + 1] = texts[i];
				}
			}
			return moveText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String toString(int[] values) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			stringBuffer.append(values[i]);
			if (i != values.length - 1) {
				stringBuffer.append(" ");
			}
		}
		return stringBuffer.toString();
	}

	private static String toString(String[] values) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			stringBuffer.append(values[i]);
			if (i != values.length - 1) {
				stringBuffer.append("\n");
			}
		}
		return stringBuffer.toString();
	}

	public static String[][][][][][] getLibrary() {
		return libraryText;
	}

	public static String[][] getChapters() {
		return libraryChapters;
	}

	public static void main (String args[]) {
		readExamplesXML(AbacusInterface.XML_FILE, null);
	}
}
