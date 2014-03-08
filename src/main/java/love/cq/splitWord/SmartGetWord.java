package love.cq.splitWord;

import love.cq.domain.SmartForest;
import love.cq.util.StringUtil;

public class SmartGetWord<T> {
	private static final String EMPTYSTRING = "";

	private SmartForest<T> forest;
	private char[] chars;
	private String str;
	public int offe;
	private int tempOffe;
	private T param;
	byte status = 0;

	private SmartForest<T> branch;

	int root = 0;
	int i = this.root;
	boolean isBack = false;

	public SmartGetWord(SmartForest<T> forest, String content) {
		this.chars = content.toCharArray();
		this.forest = forest;
		this.branch = forest;
	}

	public SmartGetWord(SmartForest<T> forest, char[] chars) {
		this.chars = chars;
		this.forest = forest;
		this.branch = forest;
	}

	public String getAllWords() {
		String temp = this.allWords();
		while (EMPTYSTRING.equals(temp)) {
			temp = this.allWords();
		}
		return temp;
	}

	public String getFrontWords() {
		String temp = this.frontWords();
		while (EMPTYSTRING.equals(temp)) {
			temp = this.frontWords();
		}
		return temp;
	}

	private String allWords() {
		if ((!this.isBack) || (this.i == this.chars.length - 1)) {
			this.i = (this.root - 1);
		}
		for (this.i += 1; this.i < this.chars.length; this.i = (this.i + 1)) {
			this.branch = this.branch.getBranch(this.chars[this.i]);
			if (this.branch == null) {
				this.root += 1;
				this.branch = this.forest;
				this.i = (this.root - 1);
				this.isBack = false;
			} else {
				switch (this.branch.getStatus()) {
				case 2:
					this.isBack = true;
					this.offe = (this.tempOffe + this.root);
					this.param = this.branch.getParam();
					return new String(this.chars, this.root, this.i - this.root + 1);
				case 3:
					this.offe = (this.tempOffe + this.root);
					this.str = new String(this.chars, this.root, this.i - this.root + 1);
					this.param = this.branch.getParam();
					this.branch = this.forest;
					this.isBack = false;
					this.root += 1;
					return this.str;
				}
			}
		}
		this.tempOffe += this.chars.length;
		return null;
	}

	private String frontWords() {
		for (; this.i < this.chars.length + 1; this.i++) {
			if (i == chars.length) {
				this.branch = null;
			} else {
				this.branch = this.branch.getBranch(this.chars[this.i]);
			}
			if (this.branch == null) {
				this.branch = this.forest;
				if (this.isBack) {
					this.offe = this.root;
					this.str = new String(this.chars, this.root, this.tempOffe);
					if ((this.root > 0) && (isE(this.chars[(this.root - 1)])) && (isE(this.str.charAt(0)))) {
						this.str = EMPTYSTRING;
					}

					if ((this.str.length() != 0) && (this.root + this.tempOffe < this.chars.length) && (isE(this.str.charAt(this.str.length() - 1)))
							&& (isE(this.chars[(this.root + this.tempOffe)]))) {
						this.str = EMPTYSTRING;
					}
					if (this.str.length() == 0) {
						this.root += 1;
						this.i = this.root;
					} else {
						this.i = (this.root + this.tempOffe);
						this.root = this.i;
					}
					this.isBack = false;

					if (EMPTYSTRING.equals(this.str)) {
						return EMPTYSTRING;
					}
					return this.str;
				}
				this.i = this.root;
				this.root += 1;
			} else {
				switch (this.branch.getStatus()) {
				case 2:
					this.isBack = true;
					this.tempOffe = (this.i - this.root + 1);
					this.param = this.branch.getParam();
					break;
				case 3:
					this.offe = this.root;
					this.str = new String(this.chars, this.root, this.i - this.root + 1);
					String temp = this.str;

					if ((this.root > 0) && (isE(this.chars[(this.root - 1)])) && (isE(this.str.charAt(0)))) {
						this.str = EMPTYSTRING;
					}

					if ((this.str.length() != 0) && (this.i + 1 < this.chars.length) && (isE(this.str.charAt(this.str.length() - 1)))
							&& (isE(this.chars[(this.i + 1)]))) {
						this.str = EMPTYSTRING;
					}
					this.param = this.branch.getParam();
					this.branch = this.forest;
					this.isBack = false;
					if (temp.length() > 0) {
						this.i += 1;
						this.root = this.i;
					} else {
						this.i = (this.root + 1);
					}
					if (EMPTYSTRING.equals(this.str)) {
						return EMPTYSTRING;
					}
					return this.str;
				}
			}
		}
		this.tempOffe += this.chars.length;
		return null;
	}

	public boolean isE(char c) {
		if (c == '.' || ((c >= 'a') && (c <= 'z'))) {
			return true;
		}
		return false;
	}

	public void reset(String content) {
		this.offe = 0;
		this.status = 0;
		this.root = 0;
		this.i = this.root;
		this.isBack = false;
		this.tempOffe = 0;
		this.chars = content.toCharArray();
		this.branch = this.forest;
	}

	/**
	 * 参数
	 * 
	 * @return
	 */
	public T getParam() {
		return this.param;
	}

	public static void main(String[] args) throws Exception {
		/**
		 * 词典的构造.一行一个词后面是参数.可以从文件读取.可以是read流.
		 */
		long start = System.currentTimeMillis();
		SmartForest<Integer> forest = new SmartForest<Integer>();

		forest.add("中国", 3);

		forest.add("android", 3);

		forest.add("java", 3);

		forest.add("中国人", 3);

		String content = " Android-java-中国人";
		content = StringUtil.rmHtmlTag(content);

		for (int i = 0; i < 1; i++) {
			SmartGetWord<Integer> udg = forest.getWord(content.toLowerCase().toCharArray());

			String temp = null;
			while ((temp = udg.getFrontWords()) != null) {
				System.out.println(temp + "\t" + udg.getParam());
			}
		}
		System.out.println(System.currentTimeMillis() - start);
	}
}
