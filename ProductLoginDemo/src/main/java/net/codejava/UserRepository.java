package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>{

	@Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email); 
     
    public User findByResetPasswordToken(String token);
   

}
