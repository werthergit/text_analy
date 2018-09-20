package com.adt.text.analy.text_analy;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TextAnaly2 {


	public static void readAndWriterTest3() throws IOException {
		File file = new File("/Users/werther/Desktop/M/test001.doc");
		String str = "";
		try {
			FileInputStream fis = new FileInputStream(file);
			HWPFDocument doc = new HWPFDocument(fis);
			String doc1 = doc.getDocumentText();
			System.out.println(doc1);
			StringBuilder doc2 = doc.getText();
			System.out.println(doc2);
			Range rang = doc.getRange();
			String doc3 = rang.text();
			System.out.println(doc3);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
	 * 文本推荐(句子级别，从一系列句子中挑出与输入句子最相似的那一个)
	 *
	 */
	@Test
	public void test14() throws IOException {
		readAndWriterTest3();
		Suggester suggester = new Suggester();
		String[] titleArray =
				(
						"威廉王子发表演说 呼吁保护野生动物\n" +
								"《时代》年度人物最终入围名单出炉 普京马云入选\n" +
								"“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
								"日本保密法将正式生效 日媒指其损害国民知情权\n" +
								"英报告说空气污染带来“公共健康危机\n"+
								"委托代理人：李晓红，北京市正见永申律师事务所律师。\n"+
								"委托代理人：王珊珊，北京市正见永申律师事务所律师。\n"+
								"法定代表人：JohnF.Coburn，III，该公司助理秘书。"
				).split("\\n");
		for (String title : titleArray)
		{
			suggester.addSentence(title);
		}

		System.out.println(suggester.suggest("发言", 1));       // 语义
		System.out.println(suggester.suggest("危机公共", 1));   // 字符
		System.out.println(suggester.suggest("mayun", 1));      // 拼音
		System.out.println(suggester.suggest("委托代理人",1));      // 字符
		System.out.println(suggester.suggest("法定",1));      // 字符
	}


}
