package cinema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;

public class Cinema {

   private final int rows;
   private final int columns;
   private final List<ReservedSeat> seats;
   private Statistics statistics;

   private final Map<UUID, CinemaController.Ticket> tickets = new HashMap<>();

   public @JsonIgnore Map<UUID, CinemaController.Ticket> getTickets() {
       return tickets;
   }

   public void storeTicket(CinemaController.Ticket ticket, UUID token) {
       tickets.put(token, ticket);
   }

   public CinemaController.Ticket getTicket(CinemaController.ReturnTokenRequest token) {
       return tickets.get(token.token());
   }

   public Cinema(int rows, int columns, List<ReservedSeat> seats) {
       this.rows = rows;
       this.columns = columns;
       this.seats = seats;

       for (int row = 1; row <= rows; row++) {
           for (int column = 1; column <= columns; column++) {
               seats.add(new ReservedSeat(false, row, column));
           }
       }

   }

   public ReservedSeat purchaseTicket(int row, int column) {
       for (ReservedSeat seat : seats) {
           if(seat.getColumn() == column && seat.getRow() == row) {
               seat.setTicket(true);
               return seat;
           }
       } return null;
   }

   public int getRows() {
       return rows;
   }

   public int getColumns() {
       return columns;
   }

   public List<ReservedSeat> getSeats() {
       return seats;
   }

   public @JsonIgnore List<ReservedSeat> getReservedSeats() {
       List<ReservedSeat> reservedSeats = new ArrayList<>();
       for(ReservedSeat seat : getSeats()){
           if (seat.getTicket()) reservedSeats.add(seat);
       }
       return reservedSeats;
   }

   public @JsonIgnore Statistics getStatistics() {
       return statistics;
   }

   public void returnTicket(CinemaController.Ticket ticket) {
       statistics.increaseIncome(-ticket.price());
       statistics.increaseBoughtSeats(-1);
   }
}