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

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class Main {




    public static void main(String[] args) throws IOException, SQLException {
        // 书册数据库驱动
        String URL = "jdbc:h2:file:/Users/a/IdeaProjects/xiedaimala-crawler/news";
        Connection connection = DriverManager.getConnection(URL, "root", "root");
        String link;
        while ((link = getNextLinkThenDelete(connection)) != null) {
                if (isLinkProcessed(connection, link)) {
                    continue;
                }
                // 我们感兴趣的，我们只处理新浪站点内的数据
                if (isInterestingLinks(link)) {
                    System.out.println(link);
                    Document doc = httpGetAndParseHtml(link);
                    parseUrlsFromPageAndStoreIntoDatabase(connection, doc);
                    // 假如这是一个新闻页面，就存入数据库，否则，就什么都不做
                    storeIntoDataBaseIfItIsNewsPage (connection, doc, link);
                    updateDatabase(connection, link, "Insert  into links_already_processed(link) values(?)");
                }
            }
        }
            private static String getNextLinkThenDelete(Connection connection) throws SQLException {
              String link =  getNextLink(connection, "select link from LINKS_TO_BE_PROCESSED LIMIT 1");
                if (link != null) {
                    updateDatabase(connection, link, "delete from links_to_be_processed where link = ?");
                }
                return link;
            }


    private static String getNextLink(Connection connection, String sql) throws SQLException {
//        List<String> results = new ArrayList<>();
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
             resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(1);
            }
        }
        return null;
    }
    private static void parseUrlsFromPageAndStoreIntoDatabase(Connection connection, Document doc) throws SQLException {
        for (Element aTag : doc.select("a")) {
            String href = aTag.attr("href");
            if (href.startsWith("//")) {
                href = "https:" + href;
            }


            if (!href.toLowerCase(Locale.ROOT).startsWith("javascript")){
                updateDatabase(connection, href, "Insert  into links_to_be_processed(link) values(?)");
            }

        }
    }

    private static boolean isLinkProcessed(Connection connection, String link) throws SQLException {

        // 询问数据库，当前链接是否被处理过了?
        try (PreparedStatement statement = connection.prepareStatement("select link from links_already_processed where link = ?")) {
            statement.setString(1, link);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
            }
        }
        return false;
    }

    private static void updateDatabase(Connection connection, String href, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, href);
            statement.executeUpdate();
        }
    }


    private static void storeIntoDataBaseIfItIsNewsPage(Connection connection, Document doc, String link) throws SQLException {
        ArrayList<Element> articleTags = doc.select("article");
        if (!articleTags.isEmpty ()) {
            for (Element articleTag : articleTags) {
                String title = articleTags.get(0).child(0).text();

                String content = articleTag.select("p").stream().map(Element::text).collect(Collectors.joining("\n"));

                // 插入数据库中

                try (PreparedStatement statement = connection.prepareStatement("insert into news (url,title,content,created_at,modified_at) values(?,?,?,now(),now())")) {
                    statement.setString(1, link);
                    statement.setString(2, title);
                    statement.setString(3, content);
                    statement.executeUpdate();
                }

            }
        }
    }

    private static Document httpGetAndParseHtml(String link) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet (link);
        httpGet.addHeader ("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        try (CloseableHttpResponse response1 = httpclient.execute (httpGet)) {
//            System.out.println (response1.getStatusLine ());

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
