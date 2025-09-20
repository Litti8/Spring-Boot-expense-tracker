package com.expensetracker.repository;

import com.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /** Find a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /** Check if a user exists by their email address.
     *
     * @param email the email address to check
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
