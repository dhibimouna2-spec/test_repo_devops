package prestify.com.prestify.web.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import prestify.com.prestify.business.services.CategorieService;
import prestify.com.prestify.business.services.OfferService;
import prestify.com.prestify.business.services.UserService;
import prestify.com.prestify.business.services.ReclamationService;
import prestify.com.prestify.dao.entities.Categorie;
import prestify.com.prestify.dao.entities.Offer;
import prestify.com.prestify.dao.entities.User;
import prestify.com.prestify.dao.entities.Reclamation;
import prestify.com.prestify.web.models.CategorieForm;
import prestify.com.prestify.web.models.OfferForm;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

    private final OfferService offerService; 
    private final CategorieService categorieService;
    private final UserService userService;
    private final ReclamationService reclamationService;

    public DashboardController(OfferService offerService, CategorieService categorieService, 
                               UserService userService, ReclamationService reclamationService){ 

        this.offerService = offerService; 
        this.categorieService = categorieService;
        this.userService = userService;
        this.reclamationService = reclamationService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("monthlyEarnings", 40000);
        model.addAttribute("annualEarnings", 215000);
        model.addAttribute("taskCompletion", 50);
        model.addAttribute("pendingRequests", 18);
        return "admin/dashboard";  
    }

  @RequestMapping("/adminofferslist")
   public String getOfferSorted(@RequestParam(required = false, defaultValue = "asc") String sortByPrix,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "3") int pageSize,
          Model model) { 

            Page<Offer> offrePage = this.offerService.getOffreSortedByPrixPagination(sortByPrix, 
                                         PageRequest.of(page, pageSize)); 
        model.addAttribute("offers",offrePage.getContent());
        model.addAttribute("sortByAge", sortByPrix);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", offrePage.getTotalPages());
        return "admin/adminofferlsist";
    }
 @RequestMapping("/admin/{id}/edit")
    public String showEditOfferForm(@PathVariable Long id, Model model) {
        
    
        Offer offreUpdate = this.offerService.getOffreById(id);
        model.addAttribute("offerForm",new OfferForm(offreUpdate.getTitle(), offreUpdate.getDescription(), offreUpdate.getPrice(), offreUpdate.getDuration(), offreUpdate.getStatus(), offreUpdate.getCategory(), offreUpdate.getIncludes(), offreUpdate.getExcludes(),offreUpdate.getLocation(), offreUpdate.getPhoneNumber()));
        model.addAttribute("categorie", this.categorieService.getAllCategories());  
        model.addAttribute("imagePath", offreUpdate.getImage()); 
                         return "admin/admineditoffer";

       
    }

    @RequestMapping(path = "/admin/{id}/edit", method = RequestMethod.POST)
public String editOffre(@Valid @ModelAttribute OfferForm offerForm,
                        BindingResult bindingResult,
                        @PathVariable Long id,
                        Model model,
                        @RequestParam MultipartFile file) {
    if (bindingResult.hasErrors()) {
        return "admin/admineditoffer";
    }

    Offer offreUpdate = this.offerService.getOffreById(id);
   

    offreUpdate.setTitle(offerForm.getTitle());
    offreUpdate.setDescription(offerForm.getDescription());
    offreUpdate.setPrice(offerForm.getPrice());
    offreUpdate.setDuration(offerForm.getDuration());
    offreUpdate.setStatus(offerForm.getStatus());
    offreUpdate.setCategory(offerForm.getCategory());
    offreUpdate.setIncludes(offerForm.getIncludes());
    offreUpdate.setExcludes(offerForm.getExcludes()); 
    offreUpdate.setLocation(offerForm.getLocation());

    if (!file.isEmpty()) {
        String fileName = file.getOriginalFilename();
        Path newFilePath = Paths.get(uploadDirectory, fileName);
        try {
            Files.write(newFilePath, file.getBytes());
            String imagePath = "/images/" + fileName;
            offreUpdate.setImage(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    this.offerService.updateOffer(offreUpdate);
    System.out.println("Offer updated successfully with ID: " + id);

    return "redirect:/adminofferslist";
}

    @GetMapping("/admincategories")
    public String listCategories(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/admincategorieslist"; // Nom du fichier HTML/Thymeleaf pour afficher la liste des groupes d'âge
    }   

    @GetMapping("/category/create")
    public String showFormForAdd(Model model) { 
       
        CategorieForm categorieForm = new CategorieForm();
        model.addAttribute("categorieForm",categorieForm);
        return "Categories/add-category"; // Nom du fichier HTML/Thymeleaf pour le formulaire d'ajout de groupe d'âge
    }   


     
    @PostMapping("/category/create")
    public String saveCategorie(@Valid @ModelAttribute("categorieForm") CategorieForm categorieForm,
            BindingResult bindingResult, Model model) {
    
        // Vérifier si la catégorie existe déjà
        if (categorieService.existsByNom(categorieForm.getNom())) {
            // Ajouter un attribut "categoryExists" à la vue
            model.addAttribute("categoryExists", true);
            return "Categories/add-category"; // Retourner à la même page avec un message d'erreur
        }
    
        // Vérifier les erreurs de validation
        if (bindingResult.hasErrors()) {
            return "Categories/add-category"; // Retourner à la même page en cas d'erreur
        }
    
        // Sauvegarder la catégorie
        categorieService.addCategorie(new Categorie(null, categorieForm.getNom()));
    
        // Rediriger vers la liste des catégories après la sauvegarde
        return "redirect:/admincategories";
    }
    
    
     @GetMapping("/categories/{id}/edit")
    public String updateCategorie(@PathVariable Long id, Model model) {
       
        Categorie categorie = categorieService.getCategorieById(id);
        model.addAttribute("categorieForm", 
                 new CategorieForm(categorie.getNom()));
                
        model.addAttribute("id", categorie.getId());
        return "Categories/edit-category"; // Nom du fichier HTML/Thymeleaf pour le formulaire de mise à jour de groupe
        // d'âge
    }  

    @PostMapping("/categories/{id}/edit")
    public String updateCategorie(@PathVariable Long id,
            @Valid @ModelAttribute("categorieForm") CategorieForm categorieForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return  "Categories/edit-category";
        }  

        categorieService.updateCategorie(id, new Categorie(id,categorieForm.getNom()));
       
        return "redirect:/admincategories"; // Rediriger vers la liste des groupes d'âge après la mise à jour
    }
   
    

    @RequestMapping(path = "categories/{id}/delete", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable Long id) {
        this.categorieService.deleteCategorie(id);
        return "redirect:/admincategories";
    }

    // ============ SUPPLIER CATEGORY MANAGEMENT ============
    @GetMapping("/supplier/categories")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String supplierListCategories(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        return "supplier/categories";
    }

    @GetMapping("/supplier/category/create")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String supplierShowFormForAdd(Model model) {
        CategorieForm categorieForm = new CategorieForm();
        model.addAttribute("categorieForm", categorieForm);
        return "supplier/add-category";
    }

    @PostMapping("/supplier/category/create")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String supplierSaveCategorie(@Valid @ModelAttribute("categorieForm") CategorieForm categorieForm,
            BindingResult bindingResult, Model model) {
        if (categorieService.existsByNom(categorieForm.getNom())) {
            model.addAttribute("categoryExists", true);
            return "supplier/add-category";
        }

        if (bindingResult.hasErrors()) {
            return "supplier/add-category";
        }

        categorieService.addCategorie(new Categorie(null, categorieForm.getNom()));
        return "redirect:/supplier/categories";
    }

    @GetMapping("/supplier/categories/{id}/edit")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String supplierUpdateCategorie(@PathVariable Long id, Model model) {
        Categorie categorie = categorieService.getCategorieById(id);
        model.addAttribute("categorieForm", new CategorieForm(categorie.getNom()));
        model.addAttribute("id", categorie.getId());
        return "supplier/edit-category";
    }

    @PostMapping("/supplier/categories/{id}/edit")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String supplierUpdateCategoriePost(@PathVariable Long id,
            @Valid @ModelAttribute("categorieForm") CategorieForm categorieForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "supplier/edit-category";
        }

        categorieService.updateCategorie(id, new Categorie(id, categorieForm.getNom()));
        return "redirect:/supplier/categories";
    }

    @RequestMapping(path = "supplier/categories/{id}/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('SUPPLIER')")
    public String supplierDeleteCategory(@PathVariable Long id) {
        this.categorieService.deleteCategorie(id);
        return "redirect:/supplier/categories";
    }

    // ============ ADMIN USER MANAGEMENT ============
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users-list";
    }

    @GetMapping("/admin/users/{id}/toggle")
    public String toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // ============ ADMIN SUPPLIER MANAGEMENT ============
    @GetMapping("/admin/suppliers")
    public String listSuppliers(Model model) {
        List<User> suppliers = userService.getUsersByRole(User.Role.SUPPLIER);
        model.addAttribute("suppliers", suppliers);
        return "admin/suppliers-list";
    }

    @GetMapping("/admin/suppliers/{id}/toggle")
    public String toggleSupplierStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin/suppliers";
    }

    @PostMapping("/admin/suppliers/{id}/delete")
    public String deleteSupplier(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/suppliers";
    }

    // ============ ADMIN CLAIMS MANAGEMENT ============
    @GetMapping("/admin/claims")
    public String listClaims(Model model) {
        List<Reclamation> claims = reclamationService.getAllReclamations();
        model.addAttribute("claims", claims);
        return "admin/claims-list";
    }

    @GetMapping("/admin/claims/{id}")
    public String viewClaim(@PathVariable Long id, Model model) {
        Reclamation claim = reclamationService.getReclamationById(id);
        model.addAttribute("claim", claim);
        return "admin/claim-details";
    }

    @PostMapping("/admin/claims/{id}/respond")
    public String respondToClaim(@PathVariable Long id, @RequestParam String response) {
        reclamationService.respondToReclamation(id, response);
        return "redirect:/admin/claims";
    }

    @PostMapping("/admin/claims/{id}/delete")
    public String deleteClaim(@PathVariable Long id) {
        reclamationService.deleteReclamation(id);
        return "redirect:/admin/claims";
    }
}
