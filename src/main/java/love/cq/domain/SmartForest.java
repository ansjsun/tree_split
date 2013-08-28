package love.cq.domain;

import java.util.Arrays;

/**
 * 一个小树,和Forest的区别是.这个在首字也是用二分查找,意味着,更节省内存.但是在构造和查找的时候都慢一点,一般应用在.词少.或者临时词典中.
 * 在ansj分词中这个应用是在自适应分词
 * 
 * @author ansj
 * 
 */
public class SmartForest<T> implements Comparable<SmartForest<T>> {

	/**
	 * status 此字的状态1，继续 2，是个词语但是还可以继续 ,3确定 nature 词语性质
	 */
	public SmartForest<T>[] branches = null;
	private char c;
	// 状态
	private byte status = 1;
	// 单独查找出来的对象
	SmartForest<T> branch = null;
	// 词典后的参数
	private T param = null;

	// root
	public SmartForest() {
	}

	// temp branch
	private SmartForest(char c) {
		this.c = c;
	}

	/**
	 * 增加子页节点
	 * 
	 * @param branch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private SmartForest<T> add(SmartForest<T> branch) {
		if (branches == null) {
			branches = new SmartForest[0];
		}
		int bs = get(branch.getC());
		if (bs > -1) {
			this.branch = this.branches[bs];
			switch (branch.getStatus()) {
			case -1:
				this.branch.setStatus(1);
				break;
			case 1:
				if (this.branch.getStatus() == 3) {
					this.branch.setStatus(2);
				}
				break;
			case 3:
				if (this.branch.getStatus() != 3) {
					this.branch.setStatus(2);
				}
				this.branch.setParam(branch.getParam());
			}
			return this.branch;
		}

		if (bs < 0) {
			SmartForest<T>[] newBranches = new SmartForest[branches.length + 1];
			int insert = -(bs + 1);
			System.arraycopy(this.branches, 0, newBranches, 0, insert);
			System.arraycopy(branches, insert, newBranches, insert + 1, branches.length - insert);
			newBranches[insert] = branch;
			this.branches = newBranches;
		}
		return branch;
	}

	public SmartForest(char c, int status, T param) {
		this.c = c;
		this.status = (byte) status;
		this.param = param;
	}

	public int get(char c) {
		if(branches==null)return -1 ;
		int i = Arrays.binarySearch(this.branches, new SmartForest<T>(c));
		return i;
	}

	/**
	 * 二分查找是否包含
	 * 
	 * @param c
	 * @return
	 */
	public boolean contains(char c) {
		if (this.branches == null) {
			return false;
		}
		return Arrays.binarySearch(this.branches, c) > -1;
	}

	public int compareTo(char c) {
		if (this.c > c)
			return 1;
		if (this.c < c) {
			return -1;
		}
		return 0;
	}

	public boolean equals(char c) {
		return this.c == c;
	}

	@Override
	public int hashCode() {
		return this.c;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = (byte) status;
	}

	public char getC() {
		return this.c;
	}

	public T getParam() {
		return this.param;
	}

	public void setParam(T param) {
		this.param = param;
	}

	/**
	 * 增加新词
	 * 
	 * @param value
	 */
	public void add(String keyWord, T t) {
		SmartForest<T> tempBranch = this;
		for (int i = 0; i < keyWord.length(); i++) {
			if (keyWord.length() == i + 1) {
				tempBranch.add(new SmartForest<T>(keyWord.charAt(i), 3, t));
			} else {
				tempBranch.add(new SmartForest<T>(keyWord.charAt(i), 1, null));
			}
			tempBranch = tempBranch.branches[tempBranch.get(keyWord.charAt(i))];
		}
	}

	public int compareTo(SmartForest<T> o) {
		// TODO Auto-generated method stub
		if (this.c > o.c)
			return 1;
		if (this.c < o.c) {
			return -1;
		}
		return 0;
	}

	/**
	 * 根据一个词获得所取的参数,没有就返回null
	 * 
	 * @param keyWord
	 */
	public SmartForest<T> getBranch(String keyWord) {
		SmartForest<T> tempBranch = this;
		int index =0 ;
		for (int j = 0; j < keyWord.length(); j++) {
			index = tempBranch.get(keyWord.charAt(j));
			if (index<0) {
				return null;
			}
			tempBranch = tempBranch.branches[index] ;
		}
		return tempBranch;
	}

	public static void main(String[] args) {
		SmartForest<Integer> sf = new SmartForest<Integer>();
		sf.add("java", 1);
		sf.add("php", 2);
		sf.add("python", 3);
		sf.add("ruby", 4);
		sf.add(".net", 5);

		SmartForest<Integer> branch2 = sf.getBranch("java");
		System.out.println(branch2.getParam());
	}

}
