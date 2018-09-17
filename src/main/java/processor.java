import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.CssSelector;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.XpathSelector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class processor implements PageProcessor {
    private String SWHelpUrl ="http://www.icafe8.com/.*";
    private String YGXHelpUrl = "http://www.yungengxin.net.cn/game/update.*";

    public void process(Page page) {
        //处理顺网逻辑
        if (page.getUrl().regex(SWHelpUrl).match()){
            int totalPage = Integer.parseInt(new JsonPathSelector("$.page.totalPage").select(page.getRawText()));
            //判断是否是第一次进入处理队列
            if ("http://www.icafe8.com/frontEnd/frontGameUpdList.do".equals(page.getUrl().toString())){
                //判断页码，如果不止一页，则添加其余页面
                System.out.println("第一次进入顺网处理逻辑。");
                if (totalPage!=1) {
                    for (int x=2;x<=totalPage;x++){
                        String addUrl = "http://www.icafe8.com/frontEnd/frontGameUpdList.do?pageNum="+x+"&gameName=";
                        page.addTargetRequest(addUrl);
                    }
                }
            }

            List<String> list = new JsonPathSelector("$.page.dataList[*]").selectList(page.getRawText());
            for (String temp:list){
                GameBean gameBean=new GameBean();
                String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                gameBean.setCompany(new JsonPathSelector(".company").select(temp).replace("[]",""));
                gameBean.setGameName(new JsonPathSelector(".gameName").select(temp));
                gameBean.setUpdateTime(new JsonPathSelector(".modifyTime").select(temp));
                gameBean.setGameType(new JsonPathSelector(".pkgClass").select(temp));
                gameBean.setGameSize(new JsonPathSelector(".pkgKbytes").select(temp));
                gameBean.setUrl(new JsonPathSelector(".url").select(temp));
                gameBean.setSource("顺网");
                gameBean.setHandleFlag("0");
                gameBean.setCrawlerTime(sdf);
                System.out.println(gameBean);
                Dao.insert(gameBean);
            }
        }

        //云更新处理逻辑。
        if(page.getUrl().regex(YGXHelpUrl).match()){
            List<String> gameList= page.getHtml().xpath("/html/body/section[3]/table/tbody/tr").all();
            for (String temp : gameList){
                System.out.println(temp);
//                new CssSelector().select(temp);
                System.out.println(new XpathSelector("//*[@class='']").select(temp));
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
                "User-Agent,Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36",
                "User-Agent,Mozilla/5.0 (Windows NT 6.1; rv,2.0.1) Gecko/20100101 Firefox/4.0.1",
                "User-Agent,Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)",
        };
        Random rand = new Random();
        return list[rand.nextInt(list.length)];
    }
}
