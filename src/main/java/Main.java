import us.codecraft.webmagic.Spider;

public class Main {
    public static void main(String[] args) {
        String SWUrl = "http://www.icafe8.com/frontEnd/frontGameUpdList.do";
        String YGXUrl = "https://yungengxin.com/game/update";
        String YLYUrl = "http://www.yileyoo.com/game/list?page=1";
        Spider spider = Spider.create(new processor());
//        spider.addUrl(SWUrl);
        spider.addUrl(YGXUrl);
//        spider.addUrl(YLYUrl);
        spider.run();
    }
}
