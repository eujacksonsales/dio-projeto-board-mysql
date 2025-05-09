package dio.projeto.board.service;


import dio.projeto.board.model.Board;
import dio.projeto.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    public Optional<Board> findByIdWithColunas(Long id){
        return boardRepository.findByIdWithColunas(id);
    }

}
