package dio.projeto.board.service;

import dio.projeto.board.model.Card;
import dio.projeto.board.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
