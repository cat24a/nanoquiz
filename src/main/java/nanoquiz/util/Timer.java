package nanoquiz.util;

public class Timer {
    private long goal;

    public Timer(long time) {
        start(time);
    }

    public void start(long time) {
        goal = System.currentTimeMillis() + time;
    }

    public long getTimeLeft() {
        return goal - System.currentTimeMillis();
    }

    public void join() throws InterruptedException {
        long timeLeft = getTimeLeft();
        if(timeLeft > 0)
            Thread.sleep(timeLeft);
    }
}
