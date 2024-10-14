public class Player {

    private int id;
    private String nickname;
    private int turnsToWait = 0;
    private boolean hasANoStoppingCard = false;

    public Player(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTurnsToWait() {
        return turnsToWait;
    }

    public void setTurnsToWait(int turnsToWait) {
        this.turnsToWait = turnsToWait;
    }

    public boolean isHasANoStoppingCard() {
        return hasANoStoppingCard;
    }

    public void setHasANoStoppingCard(boolean hasANoStoppingCard) {
        this.hasANoStoppingCard = hasANoStoppingCard;
    }
}
