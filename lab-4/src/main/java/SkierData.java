public class SkierData {
    private int time;
    private int liftID;
    private int waitTime;

    public SkierData() {
        this.time = 0;
        this.liftID = 0;
        this.waitTime = 0;
    }

    public SkierData(int time, int liftID, int waitTime) {
        this.time = time;
        this.liftID = liftID;
        this.waitTime = waitTime;
    }
}
