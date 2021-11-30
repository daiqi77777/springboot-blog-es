package com.imooc;

import com.imooc.utils.AnalyzerUtil;


import java.io.IOException;
import java.util.List;


public class test {
    public static void main(String[] args) throws IOException {
        String s = "Jewelry Sets Beaded Necklace and Beads Bracelet for Kids Girls 10 Sets Unicorn Cat Bird Owl Necklace and Beads Little Favors Bags for Girls Princess Dress Up Pretend Play";;
        List<String> str_arr = AnalyzerUtil.getAnalyzer(s);
        System.out.println(str_arr);

    }
}
