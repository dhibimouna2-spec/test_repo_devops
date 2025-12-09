package prestify.com.prestify.web.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(MethodArgumentNotValidException e, Model model) {
        // Log les erreurs de validation
        e.getBindingResult().getAllErrors().forEach(error -> {
            log.error("Validation error: " + error.getDefaultMessage());
        });

        // Ajouter le message d'erreur au modèle
        model.addAttribute("error", "Les données soumises sont invalides, veuillez vérifier les champs.");
        return "errors";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, Model model) {
        log.error("Data integrity violation: " + e.getMessage());
        model.addAttribute("error", "Erreur d'intégrité des données : " + e.getMessage());
        return "errors";
    }
}
