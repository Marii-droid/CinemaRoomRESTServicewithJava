package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

public class ReservedSeat extends Seat {
    @JsonIgnore
    private boolean ticket;

    public ReservedSeat(boolean ticket, int row, int column) {
        super(row, column);
        this.ticket = ticket;
    }

    public boolean getTicket() {
        return ticket;
    }

    public void setTicket(boolean ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReservedSeat that = (ReservedSeat) o;
        return ticket == that.ticket;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ticket);
    }
}
