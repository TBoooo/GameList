import us.codecraft.webmagic.Spider;

public class Main {
    public static void main(String[] args){
            String SWUrl="http://www.icafe8.com/frontEnd/frontGameUpdList.do";
            String YGXUrl="http://www.yungengxin.net.cn/game/update";
            Spider spider = Spider.create(new processor());
            spider.addUrl(SWUrl).addUrl(YGXUrl).run();
    }
}
