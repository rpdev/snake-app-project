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

	private Scanner scan;

	public IntegerScan(String source) {
		this.scan = new Scanner(source);
	}

	@Override
	public synchronized boolean hasNext() {
		return this.scan.hasNext("(\\{(([0-9]+)(\\,([0-9]+))*)\\})");
	}

	@Override
	public synchronized List<Integer> next() {
		ArrayList<Integer> iList = new ArrayList<Integer>();
		if (this.hasNext()) {
			System.out.println(this.scan.next("(.)")); // Read "{"
			while (this.scan.hasNextInt()) {
				
				iList.add(this.scan.nextInt());
				this.scan.next(); // Read "," if exist
			}
			//this.scan.next(); // Read "}"
		}
		iList.trimToSize();
		return iList;
	}

	@Override
	public void remove() {
	}

	@Override
	public Iterator<List<Integer>> iterator() {
		return this;
	}
}
