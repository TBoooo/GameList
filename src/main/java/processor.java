import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.VisibleForTesting;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processor implements PageProcessor {
    private boolean isWriteDb = true;
    private String SWHelpUrl = "http://www.icafe8.com/.*";
    private String YGXHelpUrl = "https://yungengxin.com/game/update.*";
    private String YLYHelpUrl = "http://www.yileyoo.com/game/list.*";
    private String datePattern = "yyyy-MM-dd HH:mm:ss";

    public void process(Page page) {
        //处理顺网逻辑
        if (page.getUrl().regex(SWHelpUrl).match()) {
            int totalPage = Integer.parseInt(new JsonPathSelector("$.page.totalPage").select(page.getRawText()));
            //判断是否是第一次进入处理队列
            if ("http://www.icafe8.com/frontEnd/frontGameUpdList.do".equals(page.getUrl().toString())) {
                //判断页码，如果不止一页，则添加其余页面
                if (totalPage != 1) {
                    for (int x = 2; x <= totalPage; x++) {
                        String addUrl = "http://www.icafe8.com/frontEnd/frontGameUpdList.do?pageNum=" + x + "&gameName=";
                        page.addTargetRequest(addUrl);
                    }
                }
            }

            List<String> list = new JsonPathSelector("$.page.dataList[*]").selectList(page.getRawText());
            for (String temp : list) {
                GameBean gameBean = new GameBean();
                String sdf = new SimpleDateFormat(datePattern).format(new Date());
                gameBean.setCompany(new JsonPathSelector(".company").select(temp).replace("[]", ""));
                gameBean.setGameName(new JsonPathSelector(".gameName").select(temp));
                gameBean.setUpdateTime(new JsonPathSelector(".modifyTime").select(temp));
                gameBean.setGameType(new JsonPathSelector(".pkgClass").select(temp));
                gameBean.setGameSize(new JsonPathSelector(".pkgKbytes").select(temp));
                gameBean.setUrl(new JsonPathSelector(".url").select(temp));
                gameBean.setSource("顺网");
                gameBean.setHandleFlag("0");
                gameBean.setCrawlerTime(sdf);
                if (isWriteDb){
                    Dao.insert(gameBean);
                }else {
                    System.out.println(gameBean);
                }
            }
        }

        //易乐游处理逻辑。
        if (page.getUrl().regex(YLYHelpUrl).match()) {

            Document doc = Jsoup.parse(page.getRawText());
            int totalDetail  = doc.select("tbody  > tr").size();
            //判断当前页面是否为15条数据，如果是则添加下一页
            if (totalDetail==15){//http://www.yileyoo.com/game/list?page=2
                int currentPage = getYLYCurrentPage(page.getUrl().toString());
                String targetUrl =  "http://www.yileyoo.com/game/list?page=" + (currentPage+1);
                page.addTargetRequest(targetUrl);
            }

            Elements eles = doc.select("tbody > tr");
            for (Element ele : eles) {
                GameBean gameBean = new GameBean();
                String sdf = new SimpleDateFormat(datePattern).format(new Date());
                gameBean.setUrl(ele.select("td:nth-child(1) > a").attr("href"));
                gameBean.setGameName(ele.select("td:nth-child(1)").text());
                gameBean.setGameType(ele.select("td:nth-child(2)").text());
                gameBean.setUpdateType(ele.select("td:nth-child(3)").text());
                gameBean.setUpdateTime(ele.select("td:nth-child(5)").text().substring(11));
                gameBean.setSource("易乐游");
                gameBean.setHandleFlag("0");
                gameBean.setCrawlerTime(sdf);
                if(isWriteDb){
                    Dao.insert(gameBean);
                }else {
                    System.out.println(gameBean);
                }
            }
        }

        //云更新处理逻辑。
        if (page.getUrl().regex(YGXHelpUrl).match()) {
            int totalPage = getYGXTotalPage(page.getHtml().toString());
            //判断是否是第一次进入处理队列
            if ("http://www.yungengxin.net.cn/game/updae".equals(page.getUrl().toString())) {
                //判断页码，如果不止一页，则添加其余页面
                if (totalPage != 1) {
                    for (int x = 2; x <= totalPage; x++) {
                        //http://www.yungengxin.net.cn/game/update?page=2&order=DESC#t-center
                        String addUrl = "http://www.yungengxin.net.cn/game/update?page=" + x + "&order=DESC#t-center";
                        page.addTargetRequest(addUrl);
                    }
                }
            }

            Document doc = Jsoup.parse(page.getRawText());
            Elements eles = doc.select("tbody > tr");
            for (Element ele : eles) {
                GameBean gameBean = new GameBean();
                String sdf = new SimpleDateFormat(datePattern).format(new Date());
                gameBean.setGameName(ele.select("td:nth-child(1)").text());
                gameBean.setUpdateTime(ele.select("td:nth-child(4)").text().substring(11));
                gameBean.setGameType(ele.select("td:nth-child(2)").text());
                gameBean.setGameSize(ele.select("td:nth-child(3)").text());
                gameBean.setSource("云更新");
                gameBean.setHandleFlag("0");
                gameBean.setCrawlerTime(sdf);
                if(isWriteDb){
                    Dao.insert(gameBean);
                }else {
                    System.out.println(gameBean);
                }
            }
        }
    }

    public Site getSite() {
        Site site = Site.me()
                .setTimeOut(60000)
                .setCycleRetryTimes(10)
                .setSleepTime(300)
                .setUserAgent(getUserAgent())
                .setRetrySleepTime(3000);
        return site;
    }

    public String getUserAgent() {
        String[] list = {
                "User-Agent, Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36",
                "User-Agent, Mozilla/5.0 (Windows NT 6.1; rv,2.0.1) Gecko/20100101 Firefox/4.0.1",
                "User-Agent, Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)",
        };
        Random rand = new Random();
        return list[rand.nextInt(list.length)];
    }

    public static int getYGXTotalPage(String temp){
        try {
            System.err.println(temp);
            Pattern p = Pattern.compile("totalPage:\\d+,");
            Matcher m = p.matcher(temp);
            m.find();
            Pattern p1 = Pattern.compile("\\d+");
            Matcher m1 = p1.matcher(m.group(0));
            m1.find();
            return Integer.parseInt(m1.group(0));
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

    }

    public static int getYLYCurrentPage(String url){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(url);
        if (m.find()){
            return Integer.parseInt(m.group(0));
        }else {
            return 0;
        }
    }
}
