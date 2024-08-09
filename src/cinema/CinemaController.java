package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CinemaController {
    private final Cinema cinema;

    public CinemaController() {
        this.cinema = new Cinema(9, 9, new ArrayList<>());
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ReturnTokenResponse purchaseSeats(@RequestBody PurchaseTicketRequest request) {
        List<ReservedSeat> seats = cinema.getReservedSeats();
        if (request.row() > cinema.getRows() || request.column() > cinema.getColumns() || request.row() < 0 || request.column() < 0) {
            throw new OutOfBoundsException("The number of a row or a column is out of bounds!");
        }
        for (ReservedSeat seat : seats) {
            if (seat.equals(new ReservedSeat(true, request.row, request.column))) {
                throw new TakenSeatException("The ticket has been already purchased!");
            }
        }
        UUID token = UUID.randomUUID();
        Seat boughtSeat = cinema.purchaseTicket(request.row, request.column);
        Ticket ticket = new Ticket (boughtSeat.getRow(), boughtSeat.getColumn(), boughtSeat.getPrice());
        cinema.storeTicket(ticket, token);

        return new ReturnTokenResponse(ticket, token);
    }

    @PostMapping("/return")
    public ReturnTicketResponse returnSeat(@RequestBody ReturnTokenRequest token) {
        Ticket ticket = cinema.getTicket(token);
        if (ticket == null) {
            throw new WrongTokenException("Wrong token!");
        }
        cinema.getTickets().remove(token.token());
        List<ReservedSeat> returnedSeat = cinema.getReservedSeats();
        for (ReservedSeat seat : returnedSeat) {
            if (seat.getRow() == ticket.row() && seat.getColumn() == ticket.column()) {
                seat.setTicket(false);
                cinema.returnTicket(ticket);
            }
        }

        return new ReturnTicketResponse(ticket);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> showStats(@RequestParam Optional<String> password){
        if (password.isPresent()) {
            Statistics stats = cinema.getStatistics();

            StatisticsResponse response = new StatisticsResponse(stats.getIncome(), stats.getAvailableSeats(), stats.getBoughtSeats());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse ("The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }
    }

    @ExceptionHandler({TakenSeatException.class, OutOfBoundsException.class, WrongTokenException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTakenSeatAndOutOfBoundsAndToken(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler ({WrongPasswordException.class})
    public ErrorResponse handleWrongPassword(WrongPasswordException e) {
        return new ErrorResponse(e.getMessage());
    }

    public record PurchaseTicketRequest(int row, int column) {}
    public record ErrorResponse(String error) {}
    public record ReturnTokenRequest(UUID token) {}
    public record ReturnTicketResponse(Ticket ticket) {}
    public record ReturnTokenResponse(Ticket ticket, UUID token) {}
    public record Ticket(int row, int column, int price){ }
    public record StatisticsResponse (int income, int available, int purchased){}
}