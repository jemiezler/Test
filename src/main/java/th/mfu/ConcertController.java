package th.mfu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import th.mfu.domain.Concert;
import th.mfu.domain.Reservation;
import th.mfu.domain.Seat;

@Controller
public class ConcertController {
    @Autowired
    ConcertRepository concertRepo;

    @Autowired
    SeatRepository seatRepo;

    @Autowired
    ReservationRepository reservationRepo;

    @GetMapping//TODO: add proper annotation for GET method
    public String book(Model model) {
        model.addAttribute("concerts", concertRepo.findAll());// TODO: list all concerts
        // TODO: return a template to list concerts
        return "list-concert";
    }

    @GetMapping("/concert/{id}/reserve-seat")//TODO: add proper annotation for GET method
    public String reserveSeatForm(@PathVariable Long concertId, Model model) {
        // TODO: add concert to model
        model.addAttribute("concert", concertRepo.findById(concertId).get());
        // TODO: add empty reservation to model
        model.addAttribute("reservation", new Reservation());
        // TODO: find available seats (booked=false) by given concert's id to the model
        List<Seat> availableSeats = seatRepo.findByBookedFalseAndConcertId(concertId);
        model.addAttribute("availableSeats", availableSeats);
        return "reserve-seat";
    }

    @Transactional
    @PostMapping("/concert/{id}/reserve-seat")
    public String reserveSeat(@ModelAttribute Reservation reservation, @PathVariable Long concertId,  Model model) {
    
        Concert concert = concertRepo.findById(concertId).get();
    
        // Get the id of the seat from the reservation
        Long seatId = reservation.getId();
    
        // Find the seat by id
        Seat selectedSeat = seatRepo.findById(seatId).get();
    
        // Set booked to true
        selectedSeat.setBooked(true);
    
        // Save seat
        seatRepo.save(selectedSeat);
    
        // Save reservation
        reservationRepo.save(reservation);
    
        return "redirect:/concerts/" + concertId;
    }
    

    /*************************************/
    /* No Modification beyond this line */
    /*************************************/

    @InitBinder
    public final void initBinderUsuariosFormValidator(final WebDataBinder binder, final Locale locale) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/concerts")
    public String listConcerts(Model model) {
        model.addAttribute("concerts", concertRepo.findAll());
        return "list-concert";
    }

    @GetMapping("/add-concert")
    public String addAConcertForm(Model model) {
        model.addAttribute("concert", new Concert());
        return "add-concert-form";
    }

    @PostMapping("/concerts")
    public String saveConcert(@ModelAttribute Concert concert) {
        concertRepo.save(concert);
        return "redirect:/concerts";
    }

    @Transactional
    @GetMapping("/delete-concert/{id}")
    public String deleteConcert(@PathVariable long id) {
        seatRepo.deleteByConcertId(id);
        concertRepo.deleteById(id);
        return "redirect:/concerts";
    }

    @GetMapping("/concerts/{id}/seats")
    public String showAddSeatForm(Model model, @PathVariable Long id) {
        model.addAttribute("seats", seatRepo.findByConcertId(id));

        Concert concert = concertRepo.findById(id).get();
        Seat seat = new Seat();
        seat.setConcert(concert);
        model.addAttribute("newseat", seat);
        return "seat-mgmt";
    }

    @PostMapping("/concerts/{id}/seats")
    public String saveSeat(@ModelAttribute Seat newseat, @PathVariable Long id) {
        Concert concert = concertRepo.findById(id).get();
        newseat.setConcert(concert);
        seatRepo.save(newseat);
        return "redirect:/concerts/" + id + "/seats";
    }
}


