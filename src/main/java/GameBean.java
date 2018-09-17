public class GameBean {
    private String company;
    private String gameName;
    private String gameType;
    private String gameSize;
    private String updateTime;
    private String updateType;
    private String crawlerTime;
    private String url;
    private String source;
    private String handleFlag;

    @Override
    public String toString() {
        return "GameBean{" +
                "company='" + company + '\'' +
                ", gameName='" + gameName + '\'' +
                ", gameType='" + gameType + '\'' +
                ", gameSize='" + gameSize + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateType='" + updateType + '\'' +
                ", crawlerTime='" + crawlerTime + '\'' +
                ", url='" + url + '\'' +
                ", source='" + source + '\'' +
                ", handleFlag='" + handleFlag + '\'' +
                '}';
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameSize() {
        return gameSize;
    }

    public void setGameSize(String gameSize) {
        this.gameSize = gameSize;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getCrawlerTime() {
        return crawlerTime;
    }

    public void setCrawlerTime(String crawlerTime) {
        this.crawlerTime = crawlerTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHandleFlag() {
        return handleFlag;
    }

    public void setHandleFlag(String handleFlag) {
        this.handleFlag = handleFlag;
    }
}
