import java.util.concurrent.atomic.AtomicLong;

public class ID  {
    AtomicLong generateID = new AtomicLong(1);

    public AtomicLong getGenerateID() {
        return generateID;
    }

    public void setGenerateID(AtomicLong generateID) {
        this.generateID = generateID;
    }

    public int newID() {
        return (int) generateID.getAndIncrement();
    }


}
