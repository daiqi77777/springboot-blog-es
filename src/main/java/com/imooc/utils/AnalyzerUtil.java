package com.imooc.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.tomcat.util.buf.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzerUtil {

    private static final String[] REPLACE_CHARS = {
            "/", "’", "|", "-",
    };
    private static final String[] WeedOutChar = {
            "for", "pieces", "with", "and",
            "inch", "inches", "x", "set",
            "party", "decor", "decoration",
            "women", "christmas", "home",
            "decorations", "birthday", "set",
            "halloween", "girls", "qty", "your",
            "and", "from", "into", "to", "diy",
            "size", "pack", "of", "by", "pairs",
            "compatible", "under", "in", "cm", "pcs",
            "pc",
    };

    /*public static List<String> getAnalyzera(String s) throws IOException {
        List<String> str_arr = new ArrayList<>();
        for (String chars : AnalyzerUtil.REPLACE_CHARS) {
            s = s.replace(chars, " ");
        }

        //创建分词对象
        Analyzer anal = new IKAnalyzer(true);
        StringReader reader = new StringReader(s);
        //分词
        TokenStream ts = anal.tokenStream("", reader);
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        //遍历分词数据
        while (ts.incrementToken()) {
            String content = term.toString();
            Pattern p = Pattern.compile(".*\\d+.*");
            Matcher m = p.matcher(content);
            if (m.matches()) {
                continue;
            }
            if (!str_arr.contains(content)) {
                str_arr.add(term.toString());
            }

        }
        reader.close();
        return str_arr;
    }*/

    private static List<String> doToken(TokenStream ts) throws IOException {
        List<String> str_arr = new ArrayList<>();
        ts.reset();
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        while (ts.incrementToken()) {
            String content = term.toString();
            Pattern p = Pattern.compile(".*\\d+.*");
            Matcher m = p.matcher(content);
            if (m.matches()) {
                continue;
            }
            if (!str_arr.contains(content)) {
                str_arr.add(term.toString());
            }
        }
        str_arr.removeIf(s -> Arrays.asList(WeedOutChar).contains(s));
        ts.end();
        ts.close();
        return str_arr;
    }

    public static List<String> getAnalyzer(String s) throws IOException {

        // Lucene core模块中的 StandardAnalyzer 英文分词器
        Analyzer ana = new StandardAnalyzer();
        TokenStream ts = ana.tokenStream("", s);
        return doToken(ts);
    }
}
