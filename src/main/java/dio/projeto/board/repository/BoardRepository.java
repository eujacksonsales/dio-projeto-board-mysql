package dio.projeto.board.repository;

import dio.projeto.board.model.Board;
import dio.projeto.board.model.Coluna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.colunas WHERE b.id = :id")
    Optional<Board> findByIdWithColunas(@Param("id") Long id);

}
