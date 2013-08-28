TreeSplitWord 一个tire树结构的分词
==================

一个trie树数据结构的分词
没有什么新意.使用方便

词典结构支持read流.字符.文件各种格式调用简单.支持不定参数

这是个例子欢迎大家使用.我用了好久了应该没有bug的

<pre><code>
		/**
		 * 词典的构造.一行一个词后面是参数.可以从文件读取.可以是read流.
		 */
		String dic = "中国\t1\tzg\n人名\t2\n中国人民\t4\n人民\t3\n孙健\t5\nCSDN\t6\njava\t7\njava学习\t10\n";
		Forest forest = Library.makeForest(new BufferedReader(new StringReader(dic)));

		/**
		 * 删除一个单词
		 */
		Library.removeWord(forest, "中国");
		/**
		 * 增加一个新词
		 */
		Library.insertWord(forest, "中国人");
		String content = "中国人名识别是中国人民的一个骄傲.孙健人民在CSDN中学到了很多最早iteye是java学习笔记叫javaeye但是java123只是一部分";
		GetWord udg = forest.getWord(content);

		String temp = null;
		while ((temp = udg.getFrontWords()) != null)
			System.out.println(temp + "\t\t" + udg.getParam(1) + "\t\t" + udg.getParam(2));
<code></pre>
