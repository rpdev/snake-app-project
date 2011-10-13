package se.chalmers.snake.snakeappwebpage.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Read int group by "{int1,int2,...,inta},...,{int1,int2,...,into}"
 * And give this a a list, the first list is all int read from the first "{...}" 
 */
public class IntegerScan implements Iterator<List<Integer>>, Iterable<List<Integer>> {

	private static final Pattern REGEX = Pattern.compile("\\{[0-9]+(\\,[0-9]+)*");
	private static final Pattern REGXPEND = Pattern.compile("\\}");
	private Scanner scan;

	/**
	 * The String to be the score of this scanner.
	 * @param source 
	 */
	public IntegerScan(String source) {
		this.scan = new Scanner(source);
		this.scan.useDelimiter(IntegerScan.REGXPEND);
	}

	@Override
	public synchronized boolean hasNext() {
		return this.scan.hasNext(IntegerScan.REGEX);
	}

	@Override
	public synchronized List<Integer> next() {
		ArrayList<Integer> iList = new ArrayList<Integer>();
		if (this.hasNext()) {
			String item = this.scan.next();
			String[] iListPost = item.substring(1, item.length()).split("\\,");
			for (String string : iListPost) {
				iList.add(Integer.parseInt(string));
			}
		}
		iList.trimToSize();
		return iList;
	}

	/**
	 * Not support on a read only IO.
	 */
	@Override
	public void remove() {
	}

	/**
	 * Return this IntergerScan and will not reset the iterator if end or has be run.
	 * @return 
	 */
	@Override
	public Iterator<List<Integer>> iterator() {
		return this;
	}
}
