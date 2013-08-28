package love.cq.library;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import love.cq.domain.Branch;
import love.cq.domain.Forest;
import love.cq.domain.Value;
import love.cq.domain.WoodInterface;
import love.cq.util.IOUtil;

public class Library {

	public static Forest makeForest(String path) throws Exception {
		return makeForest(new FileInputStream(path));
	}

	public static Forest makeForest(InputStream inputStream) throws Exception {
		return makeForest(IOUtil.getReader(inputStream, "UTF-8"));
	}

	public static Forest makeForest(BufferedReader br) throws Exception {
		return makeLibrary(br, new Forest());
	}
	
	
	/**
	 * 传入value数组.构造树
	 * 
	 * @param values
	 * @param forest
	 * @return
	 */
	public static Forest makeForest(List<Value> values) {
		Forest forest = new Forest();
		for (Value value : values) {
			insertWord(forest, value.toString());
		}
		return forest;
	}

	/**
	 * 词典树的构造方法
	 * @param br
	 * @param forest
	 * @return
	 * @throws Exception
	 */
	private static Forest makeLibrary(BufferedReader br, Forest forest) throws Exception {
		if(br==null) return forest ;
		try {
			String temp = null;
			while ((temp = br.readLine()) != null) {
				insertWord(forest, temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
		return forest;
	}

	public static void insertWord(Forest forest, Value value) {
		insertWord(forest, value.getKeyword(), value.getParamers());
	}

	/**
	 * 插入一个词
	 * 
	 * @param forest
	 * @param temp
	 */
	public static void insertWord(WoodInterface forest, String temp) {
		String[] param = temp.split("\t");

		temp = param[0];

		String[] resultParams = new String[param.length - 1];
		for (int j = 1; j < param.length; j++) {
			resultParams[j - 1] = param[j];
		}

		insertWord(forest, temp, resultParams);
	}

	private static void insertWord(WoodInterface forest, String temp, String[] param) {
		WoodInterface branch = forest;
		char[] chars = temp.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars.length == i + 1) {
				branch.add(new Branch(chars[i], 3, param));
			} else {
				branch.add(new Branch(chars[i], 1, null));
			}
			branch = branch.get(chars[i]);
		}
	}

	/**
	 * 删除一个词
	 * 
	 * @param forest
	 * @param temp
	 */
	public static void removeWord(Forest forest, String word) {
		WoodInterface branch = forest;
		char[] chars = word.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (branch == null)
				return;
			if (chars.length == i + 1) {
				branch.add(new Branch(chars[i], -1, null));
			}
			branch = branch.get(chars[i]);
		}
	}
}