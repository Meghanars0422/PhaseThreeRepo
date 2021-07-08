package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	public User getUserByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
		User user = repo.findByEmail(email);
		System.out.println(user);
        if (user != null) {
        	user.setResetPasswordToken(token);
            repo.save(user);
        } else {
            throw new UserNotFoundException("Could not find any customer with the email " + email);
        }
    }
     
    public User getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        repo.save(user);
    }
}
