package zhaw.picturePlugin.plugin;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * This class searchs images on the bing search engine for martin
 * 
 * @author marco
 *
 */
public class ImageSearch {

    Document document;

//    public static void main(String[] args) throws IOException {
//        ImageSearch image = new ImageSearch();
//        System.out.println(image.getImage("cat"));
//    }

    /**
     * search image on bing engine and return random output
     * 
     * @param type
     * @return
     * @throws IOException
     */
    public String getImage(String type) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        Random randomGenerator = new Random();
        document =
                Jsoup.connect(
                        "http://www.bing.com/images/search?q=" + type
                                + "&go=Anfrage+senden&qs=ds&form=QBLH&scope=images")
                        .userAgent(
                                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                        .get();
        Elements elements = document.select(".img_hid");
        for (int i = 0; i < elements.size(); i++) {
            String href = elements.get(i).attr("src2");
            list.add(href);
        }
        int index = randomGenerator.nextInt(list.size());
        return list.get(index);
    }
}
