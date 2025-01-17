package kz.services.epicgamesfreegamebot.repository;

import kz.services.epicgamesfreegamebot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByNewsletterTrue();
}
