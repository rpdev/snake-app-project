package se.chalmers.snake.util;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import java.util.ArrayList;

/**
 * Format text for TextView in Android.
 */
public class TextFormatter {

	private static final CharacterStyle BOLD = new android.text.style.StyleSpan(Typeface.BOLD);
	private static final CharacterStyle ITALIC = new android.text.style.StyleSpan(Typeface.ITALIC);
	private static final CharacterStyle UNDERLINE = new UnderlineSpan();

	public static class TextStyle {

		private Object textObj;
		private Object[] style;

		public TextStyle(Object text, String formatChar, String color) {
			if (text != null) {
				this.textObj = text;
				if (formatChar != null) {
					ArrayList<Object> styleList = new ArrayList<Object>(formatChar.length());
					for (int i = 0; i < formatChar.length(); i++) {
						switch (formatChar.charAt(i)) {
							case 'b':
								styleList.add(TextFormatter.BOLD);
								break;
							case 'i':
								styleList.add(TextFormatter.ITALIC);
								break;
							case 'u':
								styleList.add(TextFormatter.UNDERLINE);
								break;


						}

					}
					this.style = styleList.toArray();
				} else {
					this.style = new Object[0];
				}
			}
		}
	}

	public static TextStyle TS(Object text, String style) {
		return new TextStyle(text, style, null);
	}

	public static CharSequence FT(Object... textSeg) {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		for (Object text : textSeg) {
			if (text instanceof TextStyle) {
				TextStyle ts = (TextStyle) text;

				int startPos = ssb.length();
				ssb.append(ts.textObj.toString());
				int endPos = ssb.length();
				for (Object tsStyle : ts.style) {
					ssb.setSpan(tsStyle, startPos, endPos, 0);
				}
			} else {
				ssb.append(text.toString());
			}
		}
		return ssb;
	}
}
