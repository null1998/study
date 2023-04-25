package org.example.design;

public abstract class State {
    protected Player player;

    public State(Player player) {
        this.player = player;
    }

    /**
     * 点击锁定键
     */
    abstract void clickLock();

    /**
     * 点击播放键
     */
    abstract void clickPlay();
}

/**
 * 锁定状态
 */
class LockedState extends State {

    public LockedState(Player player) {
        super(player);
    }

    @Override
    void clickLock() {
        System.out.println("unlock");
        if (Boolean.TRUE.equals(player.getPlaying())) {
            player.setState(new PlayingState(player));
        } else {
            player.setState(new ReadyState(player));
        }
    }

    @Override
    void clickPlay() {
        throw new UnsupportedOperationException("锁定状态下不能点击播放");
    }
}

/**
 * 解锁就绪状态
 */
class ReadyState extends State {

    public ReadyState(Player player) {
        super(player);
    }

    @Override
    void clickLock() {
        System.out.println("lock");
        player.setState(new LockedState(player));
    }

    @Override
    void clickPlay() {
        System.out.println("start play");
        player.setPlaying(true);
        player.setState(new PlayingState(player));
    }
}

/**
 * 解锁播放状态
 */
class PlayingState extends State {

    public PlayingState(Player player) {
        super(player);
    }

    @Override
    void clickLock() {
        System.out.println("lock");
        player.setState(new LockedState(player));
    }

    @Override
    void clickPlay() {
        System.out.println("stop play");
        player.setPlaying(false);
        player.setState(new ReadyState(player));
    }
}

class Player {
    /**
     * 播放器状态
     */
    private State state;
    /**
     * 是否在播放歌曲
     */
    private Boolean playing;

    public Player() {
        this.setState(new LockedState(this));
        this.setPlaying(false);
    }

    /**
     * 点击锁定键
     */
    public void clickLock() {
        state.clickLock();
    }

    /**
     * 点击播放键
     */
    public void clickPlay() {
        state.clickPlay();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }
}
