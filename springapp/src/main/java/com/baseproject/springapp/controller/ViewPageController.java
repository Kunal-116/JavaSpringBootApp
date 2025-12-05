// package com.baseproject.springapp.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;

// import jakarta.servlet.http.HttpSession;

// @Controller
// public class ViewPageController {

//     @GetMapping("/")
//     public String home() {
       
//          System.out.println("✅ Home page called");
//         return "index";
//     }

//    @GetMapping("/manageExpenses")
// public String manageExpenses(HttpSession session) {

//     Object user = session.getAttribute("LOGGED_IN_USER");

//     if (user == null) {
//         return "redirect:/register"; // Not logged in → redirect
//     }

//      System.out.println("✅ Expense dashboard page called");
//         return "manage_expense_dash";
// }


//      @GetMapping("/register")
//     public String showRegistrationPage() {
//         // This returns the name of the Thymeleaf template
//         // (Spring automatically looks in /templates/)
//         return "registrationForm";
//     }

// }


// ViewPageController.java (Updated for MVC views)

package com.baseproject.springapp.controller;

import org.springframework.security.core.Authentication; // ⬅️ FIX: Missing import
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller; // Use @Controller
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.baseproject.springapp.service.UserService; // Not used in this version, but kept
import com.baseproject.springapp.util.JwtUtil;

import java.util.Map; // ⬅️ Required for the Map retrieval logic
import java.util.HashMap; // Required if you use new HashMap()

@Controller
public class ViewPageController {
    
    // The UserService dependency must be removed from the constructor 
    // or added back as a field if it's not used in this class.
    // Assuming you meant to use the JwtUtil.
    private final JwtUtil jwtUtil;

    public ViewPageController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/")
    public String home() {
        return "index"; // Assuming index.html is your main page
    }

    // Shows the registration form (you may combine login/register into one view)
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "registrationForm"; // Returns the HTML template
    }


    // This is the protected dashboard page
    @GetMapping("/manageExpenses")
    public String manageExpenses(Model model) { // ⬅️ Added Model to pass data

        Long userId = null;
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            
            Object details = authentication.getDetails();
            
            // 1. Check if the details object is the Map we stored
            if (details instanceof Map) {
                
                // 2. Cast the details to the Map
                Map<?, ?> customDetailsMap = (Map<?, ?>) details;

                // 3. Safely retrieve the User ID (it was stored as a Long)
                Object userIdObject = customDetailsMap.get("userId");
                
                if (userIdObject instanceof Long) {
                    userId = (Long) userIdObject;
                    System.out.println("Logged-in User ID (from Map): " + userId);
                    
                   
                    model.addAttribute("currentUserId", userId); 
                } else {
                    System.err.println("User ID found in map, but is not a Long. Actual type: " + (userIdObject != null ? userIdObject.getClass().getSimpleName() : "null"));
                }
            } else {
                System.err.println("Authentication details are not the expected Map type. Actual type: " + (details != null ? details.getClass().getSimpleName() : "null"));
            }
        }
        
        return "manage_expense_dash"; // Returns the protected HTML template
    }
}