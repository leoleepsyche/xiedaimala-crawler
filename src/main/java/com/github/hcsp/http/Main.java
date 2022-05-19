package com.github.hcsp.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        // 待处理的链接池
            List<String> linkPool = new ArrayList<> ();
            //已经处理过的链接池
            Set<String> processedLinks = new HashSet<> ();
            linkPool.add("https://sina.cn");

              while (true) {
                  if (linkPool.isEmpty ()) {
                      break;
                  }

                  // ArrayList 从尾部删除更有效
                  String link = linkPool.remove(linkPool.size() - 1);
                  // 判断链接是否是我们已经处理过的
                  if (processedLinks.contains (link)) {
                      continue;
                  }
                  // 我们感兴趣的，我们只处理新浪站点内的数据

                  //if ((link.contains ("news.sina.cn") || "https://sina.cn".equals (link)) && !link.contains("passport.sina.cn")){
                  if (isInterestingLinks(link)) {
                      System.out.println (link);
                      if (link.startsWith ("//")) {
                          link = "https:" + link;
                          System.out.println (link);
                      }

                      HttpGet httpGet = new HttpGet (link);
                      httpGet.addHeader ("User-Agent",
                              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                                      "Chrome/65.0.3325.181 Safari/537.36");
                      Document doc = httpGetAndParseHtml(link);
                      doc.select ("a").stream ().map (aTag->aTag.attr("href")).forEach (linkPool::add);
                      // 假如这是一个新闻页面，就存入数据库，否则，就什么都不做
                      storeIntoDataBaseIfItIsNewsPage (doc);
                      processedLinks.add (link);
                      }
                  }
    }

    private static void storeIntoDataBaseIfItIsNewsPage(Document doc) {
        ArrayList<Element> articleTags = doc.select("article");
        if (!articleTags.isEmpty ()) {
            articleTags.stream ().map (articleTag -> articleTags.get (0).child (0).text ()).forEach (System.out::println);
        }
    }

    private static Document httpGetAndParseHtml(String link) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet (link);
        httpGet.addHeader ("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        try (CloseableHttpResponse response1 = httpclient.execute (httpGet)) {
            System.out.println (response1.getStatusLine ());

            HttpEntity entity = response1.getEntity ();
            String html = EntityUtils.toString (entity);
            return Jsoup.parse (html);

        }
    }

    private static boolean isInterestingLinks(String link) {
        return (isNewsPage (link) || isIndexPage (link)) && isNotLoggedInPage (link);
    }
    private static boolean isIndexPage(String link) {
        return "https://sina.cn".equals (link);
    }
    private static boolean isNewsPage(String link) {
       return link.contains ("news.sina.cn");
    }

    private static boolean isNotLoggedInPage(String link) {
        return !link.contains("passport.sina.cn");
    }

}
