package cinema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;


public class Cinema {

   private int rows;
   private int columns;
   private List<ReservedSeat> seats;

   private final Map<UUID, CinemaController.Ticket> tickets = new HashMap<>();

   public void storeTicket(CinemaController.Ticket ticket, UUID token) {
       tickets.put(token, ticket);
   }

   public CinemaController.Ticket getTicket(CinemaController.ReturnTokenRequest token) {
       return tickets.get(token);
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
               seat.getToken();  //
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
}