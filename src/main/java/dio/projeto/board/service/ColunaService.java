package dio.projeto.board.service;

import dio.projeto.board.model.Coluna;
import dio.projeto.board.repository.ColunaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColunaService {
    private final ColunaRepository colunaRepository;

    public ColunaService(ColunaRepository colunaRepository) {
        this.colunaRepository = colunaRepository;
    }

    public Coluna save(Coluna coluna) {
        return colunaRepository.save(coluna);
    }

    public List<Coluna> findAll() {
        return colunaRepository.findAll();
    }

    public Optional<Coluna> findByIdWithCards(Long id) {
        return colunaRepository.findByIdWithCards(id);
    }
}
