package kz.services.epicgamesfreegamebot.repository;

import kz.services.epicgamesfreegamebot.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByTitle(String title);
}
