package prestify.com.prestify.business.services;
import prestify.com.prestify.dao.entities.Reclamation;
import prestify.com.prestify.dao.repositories.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

 
    public void saveReclamation(Reclamation reclamation) {
        reclamationRepository.save(reclamation);
    }

    
    public Reclamation getReclamationById(Long id) {
        return reclamationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reclamation not found"));
    }

    public void respondToReclamation(Long id, String reponseAdmin) {
        Reclamation reclamation = reclamationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reclamation not found"));
        reclamation.setReponseAdmin(reponseAdmin);
        reclamation.setStatut("répondu");  
        reclamationRepository.save(reclamation);
    }

    
    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }
    
}


