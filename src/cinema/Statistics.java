package cinema;

public class Statistics {
    int income = 0;
    int availableSeats = 81;
    int boughtSeats = 0;

    public Statistics(int income, int availableSeats, int boughtSeats) {
        this.income = income;
        this.availableSeats = availableSeats;
        this.boughtSeats = boughtSeats;
    }

    public void increaseIncome(int amount) {
        this.income += amount;
    }

    public void increaseBoughtSeats(int i){
        this.boughtSeats++;
    }

    public int getIncome() {
        return income;
    }

    public int getBoughtSeats(){
        return boughtSeats;
    }

    public int getAvailableSeats() {
        return availableSeats - boughtSeats;
    }

}
