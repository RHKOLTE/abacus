package org.tux.bagleyd.abacus.learn;

/*
 * @(#)AbacusDemo.java
 *
 * Copyright 1994 - 2015  David A. Bagley, bagleyd@tux.org
 *
 * Abacus demo and neat pointers from
 * Copyright 1991 - 1998  Luis Fernandes, elf@ee.ryerson.ca
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

import java.applet.Applet;

import org.tux.bagleyd.abacus.AbacusApplet;
import org.tux.bagleyd.abacus.AbacusCanvas;
import org.tux.bagleyd.abacus.AbacusInterface;

/**
 * The <code>AbacusDemo</code> class is shows the demonstration of
 * the <code>AbacusApplet</code> class to make it easier to learn
 * how to manipulate an abacus.
 *
 * @author	David A. Bagley
 * @author	bagleyd@tux.org
 * @author	http:/www.tux.org/~bagleyd/abacus.html
 */

/* sed "s/^/             \"/" Abacus.les | sed "s/$/\",/" */

public class AbacusDemo {
	Applet applet;
	AbacusCanvas abacus;

	boolean chapter = true, query = false, started = false;
	boolean debug = false;
	int mode = 0, lessonCount = 0, lessonLine = 0;
	int chapterCount = 0;

	public static final String nl = System.getProperty("line.separator");
	static final int MAX_LINES = 4;
	static final int CHAPTERS = 5;
	static final int INTRO = 0;
	static final int CHAPTER = 1;
	static final int QUERY = 2;
	static final int DISPLAY = 3;
	static final int CONCL = 4;

	int book = 0;
	String libraryText[][][][][][] = null;
	String chaptersFromLibrary[][] = null;

	String introFramedText[] = {
		"Click & leave the pointer in the abacus window                                 ",
		"to begin the demo.",
		"During the demo, use the Space-bar to step.",
		"Type `o' to toggle demo or `q' to quit."
	};

	String chapterText[][] = null;

	String queryText[] = {
		"Type: `n' for the Next lesson                                                  ",
		"      `r' to Repeat previous lesson",
		"      `j' to Jump to next chapter",
		"      `q' to Quit the demo",
	};

	String conclLesson[] = {
		"Here Endth the Lesson",
		"",
		"",
		""
	};
	String displayText[] = new String[MAX_LINES];


	public AbacusDemo(Applet applet, AbacusCanvas abacus) {
		this.applet = applet;
		this.abacus = abacus;

		AbacusExamples.setLibrary(AbacusInterface.XML_FILE,
			((AbacusApplet.getApplication()) ? null : applet.getCodeBase()));
		libraryText = AbacusExamples.getLibrary();
		chaptersFromLibrary = AbacusExamples.getChapters();
		makeChapterText(chaptersFromLibrary[book]);
	}

	private void makeChapterText(String[] text) {
		chapterText = new String[text.length][MAX_LINES];
		for (int i = 0; i < text.length; i++) {
		    // First line intentionally long
			chapterText[i][0] = "     " + text[i]
				+ " Chapter                                                       ";
			chapterText[i][1] = "Type: `r' to Read this chapter";
			chapterText[i][2] = "      `n' to Next chapter";
			chapterText[i][3] = "      `q' to Quit the demo";
		}
	}

	public boolean queryChapter() {
		return chapter;
	}

	public void drawText(int textType) {
		int line;
		String[] tempText = new String[MAX_LINES];

		for (line = 0; line < MAX_LINES; line++) {
			switch (textType) {
			case INTRO:
				tempText[line] = introFramedText[line];
				if (debug)
					System.out.println(" INTRO: " +
						tempText[line]);
				chapter  = true;
				break;
			case CHAPTER:
				tempText[line] =
					chapterText[chapterCount][line];
				if (debug)
					System.out.println(" CHAPTER: " +
						tempText[line]);
				break;
			case CONCL:
				started = false;
				tempText[line] = conclLesson[line];
				if (debug)
					System.out.println(" CONCL: " +
						tempText[line]);
				break;
			case QUERY:
				query = true;
				tempText[line] = queryText[line];
				if (debug)
					System.out.println(" QUERY: " +
						tempText[line]);
				break;
			default:
				tempText[line] = displayText[line];
				if (debug)
					System.out.println(" DISPLAY: " +
						tempText[line]);
				break;
			}
		}
		((AbacusApplet)applet).setDemoMsg(tempText[0],
			tempText[1], tempText[2], tempText[3]);
	}

	public void startLesson() {
		lessonLine = 0;
		mode = abacus.getMode();
		if (mode == AbacusInterface.Modes.Roman.ordinal())
			mode = AbacusInterface.Modes.Japanese.ordinal();
		else if (mode == AbacusInterface.Modes.Danish.ordinal())
			mode = AbacusInterface.Modes.Russian.ordinal();
		else if (mode == AbacusInterface.Modes.Medieval.ordinal())
			mode = AbacusInterface.Modes.Chinese.ordinal();
		/*else if (mode == AbacusInterface.Modes.Generic.ordinal())
			mode = AbacusInterface.Modes.Chinese.ordinal();*/
	}

	public void doLesson() {
		int line;
		String[] strings =
			libraryText[book][mode][chapterCount][lessonCount][lessonLine][0].split(" ");
		int aux, deck, rail, number;

		if (strings.length >= 4) {
			aux = Integer.parseInt(strings[0]);
			deck = Integer.parseInt(strings[1]);
			rail = Integer.parseInt(strings[2]);
			number = Integer.parseInt(strings[3]);
		} else {
			System.err.println("AbacusDemo: size " + strings.length);
			return;
		}
		if (debug)
			System.out.println("aux = " + aux +
				", deck = " + deck +
				", rail = " + rail +
				", number = " + number);
		abacus.abacusMove(aux, deck, rail, number);
		for (line = 0; line < MAX_LINES; line++) {
			displayText[line] =
				libraryText[book][mode][chapterCount][lessonCount][lessonLine][line + 1];
		}
		lessonLine++;
		if (debug)
			System.out.println(" doLesson");
		if (lessonLine == libraryText[book][mode][chapterCount][lessonCount].length) {
			drawText(CONCL);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				/* Ignore */
			}
			drawText(QUERY);
			lessonLine = 0;
			abacus.clearRails();
		} else if (started) {
			if (chapter) {
				drawText(CHAPTER);
			} else {
				drawText(DISPLAY);
			}
		} else if (query) {
			drawText(QUERY);
		} else {
			drawText(INTRO);
		}
	}

	public void clearDemo() {
		started = true;
		query = false;
		startLesson();
		doLesson();
	}

	public void queryDemo(boolean chapt, boolean advance) {
		if (debug)
			System.out.println("queryDemo: chapt " + chapt +
				", advance " + advance);
		started = true;
		if (chapt) {
			if (advance) {
				chapterCount++;
				if (chapterCount >= CHAPTERS)
					chapterCount = 0;
				drawText(CHAPTER);
			} else {
				/* got a Read */
				chapter = false;
				lessonCount = 0;
				query = false;
				startLesson();
				doLesson();
			}
		} else {
			if (query) {
				started = true;
				query = false;
				if (advance) {
					lessonCount++;
					if (lessonCount >= libraryText[0][mode][chapterCount].length) {
						lessonCount = 0;
						chapterCount++;
						if (chapterCount >= CHAPTERS)
							chapterCount = 0;
						chapter = true;
						drawText(CHAPTER);
					}
				}
				startLesson();
				doLesson();
			}
		}
	}

	public void jumpDemo() {
		if (!chapter) {
			chapter = true;
			started = true;
			chapterCount++;
			if (chapterCount >= CHAPTERS)
				chapterCount = 0;
			drawText(CHAPTER);
		}
        }

	public void chapterDemo(int chapt) {
		if (started && chapter) {
			chapterCount = chapt;
			if (chapterCount < 0  || chapterCount >= CHAPTERS)
				chapterCount = 0;
			drawText(CHAPTER);
		}
	}

	public void moreDemo() {
		if (!chapter && started && !query) {
			doLesson();
		}
	}
}
