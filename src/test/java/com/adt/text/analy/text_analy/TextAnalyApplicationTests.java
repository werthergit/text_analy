package com.adt.text.analy.text_analy;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TextAnalyApplicationTests {


	@Test
	public void contextLoads() {

		System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！"));

	}

	/**
	 * 标准分词
	 */
	@Test
	public void test02(){

		List<Term> termList = StandardTokenizer.segment("商品和服务");
		System.out.println(termList);
	}

	@Test
	public void test03(){
		CRFLexicalAnalyzer analyzer = null;
		try {
			analyzer = new CRFLexicalAnalyzer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] tests = new String[]{
				"商品和服务",
				"上海华安工业（集团）公司董事长谭旭光和秘书胡花蕊来到美国纽约现代艺术博物馆参观",
				"微软公司於1975年由比爾·蓋茲和保羅·艾倫創立，18年啟動以智慧雲端、前端為導向的大改組。" // 支持繁体中文
		};
		for (String sentence : tests)
		{
			System.out.println(analyzer.analyze(sentence));
		}
	}

	/**
	 * 中国人名识别
	 */
	@Test
	public void test04(){
		String[] testCase = new String[]{
				"签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
				"王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
				"张浩和胡健康复员回家了",
				"王总和小丽结婚了",
				"编剧邵钧林和稽道青说",
				"这里有关天培的有关事迹",
				"龚学平等领导,邓颖超生前",
				"赵敬诚是个好孩子",
		};
		Segment segment = HanLP.newSegment().enableNameRecognize(true);
		for (String sentence : testCase)
		{
			List<Term> termList = segment.seg(sentence);
			System.out.println(termList);
		}
	}

	/**
	 * 音译人名识别
	 */
	@Test
	public void test05(){
		String[] testCase = new String[]{
				"一桶冰水当头倒下，微软的比尔盖茨、Facebook的扎克伯格跟桑德博格、亚马逊的贝索斯、苹果的库克全都不惜湿身入镜，这些硅谷的科技人，飞蛾扑火似地牺牲演出，其实全为了慈善。",
				"世界上最长的姓名是凯文·加内特,简森·乔伊·亚历山大·比基·卡利斯勒·达夫·埃利奥特·福克斯·伊维鲁莫·马尔尼·梅尔斯·帕特森·汤普森·华莱士·普雷斯顿。",
		};
		Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true);
		for (String sentence : testCase)
		{
			List<Term> termList = segment.seg(sentence);
			System.out.println(termList);
		}

	}

	/**
	 * 机构名识别
	 */
	@Test
	public void test06(){
		String[] testCase = new String[]{
				"我在上海林原科技有限公司兼职工作，",
				"我经常在台川喜宴餐厅吃饭，",
				"偶尔去地中海影城看电影。",
				"每周六去钦州石油分公司和北京车和家有限公司办公。",
				"委托代理人：李晓红，北京市正见永申律师事务所律师。",
				"委托代理人：王珊珊，北京市正见永申律师事务所律师。",
				"法定代表人：JohnF.Coburn，III，该公司助理秘书。"

		};
		Segment segment = HanLP.newSegment().enableOrganizationRecognize(true).enablePlaceRecognize(true).enableTranslatedNameRecognize(true);
		for (String sentence : testCase)
		{
			List<Term> termList = segment.seg(sentence);
			for(Term term : termList){
				if(term.nature.toString().equals("nr")){
					System.out.println("姓名："+term.word);
				}

			}
			System.out.println(termList);
		}
	}

	/**
	 * 地名识别
	 */
	@Test
	public void test07(){
		String[] testCase = new String[]{
				"武胜县新学乡政府大楼门前锣鼓喧天",
				"蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机",
		};
		Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
		for (String sentence : testCase)
		{
			List<Term> termList = segment.seg(sentence);
			System.out.println(termList);
		}

	}

	/**
	 * 关键词提取
	 */
	@Test
	public void test08(){
		String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
		List<String> keywordList = HanLP.extractKeyword(content, 5);
		System.out.println(keywordList);
	}

	/**
	 * 自动摘要
	 */
	@Test
	public void test09(){
		String document = "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
				"算法可以宽泛的分为三类，\n" +
				"一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
				"二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
				"三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
		List<String> sentenceList = HanLP.extractSummary(document, 3);
		System.out.println(sentenceList);
	}


	/**
	 * 自动摘要
	 */
	@Test
	public void test10(){
		CoNLLSentence sentence = HanLP.parseDependency("委托代理人：李晓红，北京市正见永申律师事务所律师。\n");
		System.out.println(sentence);
		// 可以方便地遍历它
		for (CoNLLWord word : sentence)
		{
			System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
		}
		// 也可以直接拿到数组，任意顺序或逆序遍历
		CoNLLWord[] wordArray = sentence.getWordArray();
		for (int i = wordArray.length - 1; i >= 0; i--)
		{
			CoNLLWord word = wordArray[i];
			System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
		}
		// 还可以直接遍历子树，从某棵子树的某个节点一路遍历到虚根
		CoNLLWord head = wordArray[12];
		while ((head = head.HEAD) != null)
		{
			if (head == CoNLLWord.ROOT) System.out.println(head.LEMMA);
			else System.out.printf("%s --(%s)--> ", head.LEMMA, head.DEPREL);
		}

	}

	/**
	 * 最短路径
	 */
	@Test
	public void test11(){
		Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
		Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
		String[] testCase = new String[]{
				"今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
				"刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
		};
		for (String sentence : testCase)
		{
			System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
		}
	}


	/**
	 * 短语提取
	 */
	@Test
	public void test13(){
		String text = "算法工程师\n" +
				"算法（Algorithm）是一系列解决问题的清晰指令，也就是说，能够对一定规范的输入，在有限时间内获得所要求的输出。如果一个算法有缺陷，或不适合于某个问题，执行这个算法将不会解决这个问题。不同的算法可能用不同的时间、空间或效率来完成同样的任务。一个算法的优劣可以用空间复杂度与时间复杂度来衡量。算法工程师就是利用算法处理事物的人。\n" +
				"\n" +
				"1职位简介\n" +
				"算法工程师是一个非常高端的职位；\n" +
				"专业要求：计算机、电子、通信、数学等相关专业；\n" +
				"学历要求：本科及其以上的学历，大多数是硕士学历及其以上；\n" +
				"语言要求：英语要求是熟练，基本上能阅读国外专业书刊；\n" +
				"必须掌握计算机相关知识，熟练使用仿真工具MATLAB等，必须会一门编程语言。\n" +
				"\n" +
				"2研究方向\n" +
				"视频算法工程师、图像处理算法工程师、音频算法工程师 通信基带算法工程师\n" +
				"\n" +
				"3目前国内外状况\n" +
				"目前国内从事算法研究的工程师不少，但是高级算法工程师却很少，是一个非常紧缺的专业工程师。算法工程师根据研究领域来分主要有音频/视频算法处理、图像技术方面的二维信息算法处理和通信物理层、雷达信号处理、生物医学信号处理等领域的一维信息算法处理。\n" +
				"在计算机音视频和图形图像技术等二维信息算法处理方面目前比较先进的视频处理算法：机器视觉成为此类算法研究的核心；另外还有2D转3D算法(2D-to-3D conversion)，去隔行算法(de-interlacing)，运动估计运动补偿算法(Motion estimation/Motion Compensation)，去噪算法(Noise Reduction)，缩放算法(scaling)，锐化处理算法(Sharpness)，超分辨率算法(Super Resolution),手势识别(gesture recognition),人脸识别(face recognition)。\n" +
				"在通信物理层等一维信息领域目前常用的算法：无线领域的RRM、RTT，传送领域的调制解调、信道均衡、信号检测、网络优化、信号分解等。\n" +
				"另外数据挖掘、互联网搜索算法也成为当今的热门方向。\n" +
				"算法工程师逐渐往人工智能方向发展。";
		List<String> phraseList = HanLP.extractPhrase(text, 10);
		System.out.println(phraseList);
	}

	/**
	 * 文本推荐(句子级别，从一系列句子中挑出与输入句子最相似的那一个)
	 *
	 */
	@Test
	public void test14(){
		Suggester suggester = new Suggester();
		String[] titleArray =
				(
						"威廉王子发表演说 呼吁保护野生动物\n" +
								"《时代》年度人物最终入围名单出炉 普京马云入选\n" +
								"“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
								"日本保密法将正式生效 日媒指其损害国民知情权\n" +
								"英报告说空气污染带来“公共健康危机”"
				).split("\\n");
		for (String title : titleArray)
		{
			suggester.addSentence(title);
		}

		System.out.println(suggester.suggest("发言", 1));       // 语义
		System.out.println(suggester.suggest("危机公共", 1));   // 字符
		System.out.println(suggester.suggest("mayun", 1));      // 拼音
	}


}
