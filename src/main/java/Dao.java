public class Dao {
    public static void insert(GameBean gameBean) {
        DbUtil.MysqlUpdate("insert into game_list(company,game_name,game_type,game_size,update_time,update_type,crawler_time,game_url,game_source) VALUES (?,?,?,?,?,?,?,?,?)", gameBean.getCompany(), gameBean.getGameName(),
                gameBean.getGameType(), gameBean.getGameSize(), gameBean.getUpdateTime(), gameBean.getUpdateType(),
                gameBean.getCrawlerTime(), gameBean.getUrl(), gameBean.getSource());
    }
}
