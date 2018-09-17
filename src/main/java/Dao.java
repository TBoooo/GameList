public class Dao {
    public static void insert(GameBean gameBean){
        DbUtil.MysqlUpdate("replace into game_list VALUES (?,?,?,?,?,?,?,?,?,?)",gameBean.getCompany(),gameBean.getGameName(),
                gameBean.getGameType(),gameBean.getGameSize(),gameBean.getUpdateTime(),gameBean.getUpdateType(),
                gameBean.getCrawlerTime(),gameBean.getUrl(),gameBean.getSource(),gameBean.getHandleFlag());
    }
}
