package cinema;

public class Statistics {
    int income = 0;
    int availableSeats = 81;
    int boughtSeats = 0;

    public Statistics() {
    }

    public void increaseIncome(int amount) {
        this.income += amount;
    }

    public void increaseBoughtSeats() {
        this.boughtSeats++;
    }

    public void decreaseBoughtSeats() {
        this.boughtSeats--;
    }

    public void increaseAvailableSeats() {
        this.availableSeats++;
    }

    public void decreaseAvailableSeats() {
        this.availableSeats--;
    }

    public int getIncome() {
        return income;
    }

    public int getBoughtSeats() {
        return boughtSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

}
