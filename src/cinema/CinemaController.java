package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class CinemaController {
    private final Cinema cinema;

    //
    public CinemaController() {
        this.cinema = new Cinema(9, 9, new ArrayList<>());
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public Seat purchaseSeats(@RequestBody PurchaseTicketRequest request) {
        List<ReservedSeat> seats = cinema.getReservedSeats();
        if (request.row() > cinema.getRows() || request.column() > cinema.getColumns() || request.row() < 0 || request.column() < 0) {
            throw new OutOfBoundsException("The number of a row or a column is out of bounds!");
        }
        for (ReservedSeat seat : seats) {
            if (seat.equals(new ReservedSeat(true, request.row, request.column))) {
                throw new TakenSeatException("The ticket has been already purchased!");
            }
        }

        return cinema.purchaseTicket(request.row, request.column, cinema.getToken);  //nog ergens opslaan?
    }

    @PostMapping("/return")
    public ReturnTicketResponse returnSeat(@RequestBody ReturnTokenRequest token) {
        Ticket ticket = cinema.getTicket(token);
        if (ticket == null) {
            throw new WrongTokenException("Wrong token!");
        }
        return new ReturnTicketResponse(ticket);
    }

    @ExceptionHandler({TakenSeatException.class, OutOfBoundsException.class, WrongTokenException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTakenSeatAndOutOfBoundsAndToken(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    public record PurchaseTicketRequest(int row, int column) {    }
    public record ErrorResponse(String error) {    }
    public record ReturnTokenRequest(UUID token) {    }
    public record ReturnTicketResponse(Ticket ticket) {    }
    public record Ticket(int row, int column, int price){ }
}