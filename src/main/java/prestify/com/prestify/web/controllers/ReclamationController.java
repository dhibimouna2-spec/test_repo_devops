package prestify.com.prestify.web.controllers;

import prestify.com.prestify.dao.entities.Reclamation;
import prestify.com.prestify.business.services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    // Liste des réclamations pour l'admin
    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String listReclamations(Model model) {
        model.addAttribute("reclamations", reclamationService.getAllReclamations());
        return "admin/adminlistreclamation";  // Vue pour l'admin
    }

    // Formulaire pour ajouter une réclamation (pour le client)
    @GetMapping("/client/create")
    @PreAuthorize("hasAnyRole('CLIENT', 'SUPPLIER')")
    public String createReclamationForm(Model model) {
        model.addAttribute("reclamation", new Reclamation());
        return "reclamation/create";  // Vue pour le client
    }

    // Soumettre la réclamation (client)
    @PostMapping("/client/save")
    @PreAuthorize("hasAnyRole('CLIENT', 'SUPPLIER')")
    public String saveReclamation(@ModelAttribute Reclamation reclamation) {
        reclamation.setStatut("en cours");  // Initialiser le statut de la réclamation
        reclamationService.saveReclamation(reclamation);
        return "redirect:/client/create";  // Redirection vers la liste des réclamations
    }

    // Gérer une réclamation (réponse de l'admin)
    @GetMapping("/admin/reclamation/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewReclamationForAdmin(@PathVariable Long id, Model model) {
        Reclamation reclamation = reclamationService.getReclamationById(id);
        model.addAttribute("reclamation", reclamation);
        return "manage";  // Vue pour gérer la réclamation
    }

    // Répondre à une réclamation (admin)
    @PostMapping("/admin/reclamation/{id}/respond")
    public String respondToReclamation(@PathVariable Long id, @RequestParam String reponseAdmin) {
        reclamationService.respondToReclamation(id, reponseAdmin);
        return "redirect:/admin/list";  // Redirection après réponse
    }
}
