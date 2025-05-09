package dio.projeto.board.repository;

import dio.projeto.board.model.Coluna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ColunaRepository extends JpaRepository<Coluna, Long> {
    @Query("SELECT c FROM Coluna c LEFT JOIN FETCH c.cards WHERE c.id = :id")
    Optional<Coluna> findByIdWithCards(@Param("id") Long id);
}
