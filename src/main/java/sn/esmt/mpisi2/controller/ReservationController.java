package sn.esmt.mpisi2.controller;


import sn.esmt.mpisi2.DTO.UserWithRoleDTO;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.*;
import sn.esmt.mpisi2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservationController {

    @Autowired
    ReservationServiceImpl reservationService;




    @PostMapping("/updateEmprunteurReservation/{id}")
    public String updateEmprunteur(@PathVariable("id") int id,
                                   @RequestParam("emprunteurId") int emprunteurId) {
        reservationService.updateEmprunteurId(id, emprunteurId);
        return "redirect:/dashboard/my_reservations"; // Redirigez vers une page appropriée après la mise à jour
    }

    @PostMapping("/updatedReservation/{id}")
    public String updatedReservation(@PathVariable("id") int id, @RequestParam("emprunteurId") long emprunteurId, @ModelAttribute("updatedReservation") Reservation updatedReservation) {
        reservationService.updateReservation(id, emprunteurId, updatedReservation);
        return "redirect:/dashboard/my_reservations";
    }


    @RequestMapping("/deleteMyReservationList/{id}")
    public String deleteMyReservationList(@PathVariable("id") int id) {
        reservationService.deleteById(id);
        return "redirect:/dashboard/my_reservations";
    }

    @RequestMapping("/editMyReservationList/{id}")
    public String editMyReservationList(@PathVariable("id") int id, Model model) {
        Reservation b=reservationService.getReservationById(id);
        model.addAttribute("reservation",b);
        return "ListReservationEdit";
        //return "redirect:/my_books";
    }

}
