package prestify.com.prestify.web.controllers;

import prestify.com.prestify.dao.entities.Reclamation;
import prestify.com.prestify.business.services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    // Liste des réclamations pour l'admin
    @GetMapping ("/list")
    public String listReclamations(Model model) {
        model.addAttribute("reclamations", reclamationService.getAllReclamations());
        return "admin/adminlistreclamation";  // Vue pour l'admin
    }

    // Formulaire pour ajouter une réclamation (pour le client)
    @GetMapping("/create")
    public String createReclamationForm(Model model) {
        model.addAttribute("reclamation", new Reclamation());
        return "reclamation/create";  // Vue pour le client
    }

    // Soumettre la réclamation (client)
    @PostMapping("/save")
    public String saveReclamation(@ModelAttribute Reclamation reclamation) {
        reclamation.setStatut("en cours");  // Initialiser le statut de la réclamation
        reclamationService.saveReclamation(reclamation);
        return "redirect:/create";  // Redirection vers la liste des réclamations
    }

    // Gérer une réclamation (réponse de l'admin)
    @GetMapping("/admin/{id}")
    public String viewReclamationForAdmin(@PathVariable Long id, Model model) {
        Reclamation reclamation = reclamationService.getReclamationById(id);
        model.addAttribute("reclamation", reclamation);
        return "manage";  // Vue pour gérer la réclamation
    }

    // Répondre à une réclamation (admin)
    @PostMapping("/admin/{id}/respond")
    public String respondToReclamation(@PathVariable Long id, @RequestParam String reponseAdmin) {
        reclamationService.respondToReclamation(id, reponseAdmin);
        return "redirect:/reclamations";  // Redirection après réponse
    }
}
