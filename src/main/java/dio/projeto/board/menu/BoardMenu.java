package dio.projeto.board.menu;

import dio.projeto.board.model.Board;
import dio.projeto.board.model.Card;
import dio.projeto.board.model.Coluna;
import dio.projeto.board.model.TipoColuna;
import dio.projeto.board.service.BoardService;
import dio.projeto.board.service.CardService;
import dio.projeto.board.service.ColunaService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class BoardMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final BoardService boardService;
    private final ColunaService colunaService;
    private final CardService cardService;

    public BoardMenu(BoardService boardService, ColunaService colunaService, CardService cardService) {
        this.boardService = boardService;
        this.colunaService = colunaService;
        this.cardService = cardService;
    }

    public void start() {
        while (true) {
            System.out.println("1. Criar novo board");
            System.out.println("2. Selecionar board");
            System.out.println("3. Excluir boards");
            System.out.println("4. Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    criarNovoBoard();
                    break;
                case 2:
                    Board boardSelecionado = selecionarBoard();
                    if(boardSelecionado != null){
                        menuBoard(boardSelecionado.getId());
                    }
                    break;
                case 3:
                    excluirBoards();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public void criarNovoBoard() {
        System.out.print("Digite o nome do novo board: ");
        String nome = scanner.nextLine();

        Board board = new Board();
        board.setNome(nome);

        Set<Coluna> colunas = new HashSet<>();

        // Coluna Inicial
        Coluna inicial = new Coluna();
        inicial.setNome("A Fazer");
        inicial.setOrdem(1);
        inicial.setTipo(TipoColuna.INICIAL);
        inicial.setBoard(board);
        colunas.add(inicial);

        // Colunas Pendentes (usuário pode adicionar mais)
        System.out.print("Quantas colunas pendentes deseja adicionar? ");
        int qtdPendentes = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < qtdPendentes; i++) {
            System.out.print("Nome da coluna pendente " + (i + 1) + ": ");
            String nomePendente = scanner.nextLine();
            Coluna pendente = new Coluna();
            pendente.setNome(nomePendente);
            pendente.setTipo(TipoColuna.PENDENTE);
            pendente.setBoard(board);
            pendente.setOrdem(i + 2); // após a inicial
            colunas.add(pendente);
        }

        // Coluna Final
        Coluna finalCol = new Coluna();
        finalCol.setNome("Concluído");
        finalCol.setTipo(TipoColuna.FINAL);
        finalCol.setBoard(board);
        finalCol.setOrdem(colunas.size() + 1);
        colunas.add(finalCol);

        // Coluna Cancelamento
        Coluna cancelamento = new Coluna();
        cancelamento.setNome("Cancelado");
        cancelamento.setTipo(TipoColuna.CANCELAMENTO);
        cancelamento.setBoard(board);
        cancelamento.setOrdem(colunas.size() + 2);
        colunas.add(cancelamento);

        board.setColunas(colunas);

        boardService.save(board);
        System.out.println("Board criado com sucesso!");
    }

    public Board selecionarBoard() {
        List<Board> boards = boardService.findAll();
        if (boards.isEmpty()) {
            System.out.println("Nenhum board cadastrado.");
            return null;
        }
        System.out.println("Selecione o board:");
        for (int i = 0; i < boards.size(); i++) {
            System.out.println((i + 1) + ". " + boards.get(i).getNome());
        }
        int opcao = scanner.nextInt();
        scanner.nextLine();
        if (opcao < 1 || opcao > boards.size()) {
            System.out.println("Opção inválida.");
            return null;
        }
        return boards.get(opcao - 1);
    }


    public void excluirBoards() {
        List<Board> boards = boardService.findAll();
        if (boards.isEmpty()) {
            System.out.println("Nenhum board para excluir.");
            return;
        }
        System.out.println("Selecione o board para excluir:");
        for (int i = 0; i < boards.size(); i++) {
            System.out.println((i + 1) + ". " + boards.get(i).getNome());
        }
        int opcao = scanner.nextInt();
        scanner.nextLine();
        if (opcao < 1 || opcao > boards.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        Board board = boards.get(opcao - 1);
        boardService.deleteById(board.getId());
        System.out.println("Board excluído com sucesso!");
    }

    public void menuBoard(Long boardId) {
        while (true) {
            Board board = encontrarBoard(boardId);
            System.out.println("Gerenciando o board: " + board.getNome());
            System.out.println("1. Mover card para próxima coluna");
            System.out.println("2. Cancelar card");
            System.out.println("3. Criar card");
            System.out.println("4. Bloquear card");
            System.out.println("5. Desbloquear card");
            System.out.println("6. Fechar board");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    moverCard(board.getId());
                    break;
                case 2:
                    cancelarCard(board.getId());
                    break;
                case 3:
                    criarCard(board.getId());
                    break;
                case 4:
                    bloquearCard(board.getId());
                    break;
                case 5:
                    desbloquearCard(board.getId());
                    break;
                case 6:
                    return; // Sai do menu de manipulação e volta ao menu principal
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public void criarCard(Long boardId) {
        System.out.print("Título do card: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição do card: ");
        String descricao = scanner.nextLine();

        Board board = encontrarBoard(boardId);
        // Encontrar a coluna inicial do board
        Coluna colunaInicial = board.getColunas().stream()
                .filter(c -> c.getTipo() == TipoColuna.INICIAL)
                .findFirst()
                .orElse(null);

        if (colunaInicial == null) {
            System.out.println("Coluna inicial não encontrada!");
            return;
        }

        Card card = new Card();
        card.setTitulo(titulo);
        card.setDescricao(descricao);
        card.setDataCriacao(LocalDateTime.now());
        card.setBloqueado(false);
        card.setColuna(colunaInicial);

        cardService.save(card);
        System.out.println("Card criado na coluna inicial!");
    }

    public void moverCard(Long boardId) {
        Scanner scanner = new Scanner(System.in);

        Board board = encontrarBoard(boardId);
        // Listar todas as colunas e cards
        Set<Coluna> colunas = board.getColunas();
        List<Card> cards = new ArrayList<>();
        for (Coluna coluna : colunas) {
            Coluna colunaComCards = colunaService.findByIdWithCards(coluna.getId()).orElse(coluna);
            cards.addAll(colunaComCards.getCards());
        }

        if (cards.isEmpty()) {
            System.out.println("Não há cards para mover.");
            return;
        }

        // Listar cards
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            System.out.println((i + 1) + ". " + c.getTitulo() + " (Coluna: " + c.getColuna().getNome() + ")" + (c.isBloqueado() ? " [BLOQUEADO]" : ""));
        }

        System.out.print("Selecione o card para mover: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao < 1 || opcao > cards.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Card card = cards.get(opcao - 1);

        if (card.isBloqueado()) {
            System.out.println("Card está bloqueado e não pode ser movido.");
            return;
        }

        Coluna colunaAtual = card.getColuna();
        int ordemAtual = colunaAtual.getOrdem();

        // Encontrar próxima coluna (exceto cancelamento)
        Optional<Coluna> proximaColuna = colunas.stream()
                .filter(c -> c.getOrdem() == ordemAtual + 1 && c.getTipo() != TipoColuna.CANCELAMENTO)
                .findFirst();

        if (proximaColuna.isPresent()) {
            card.setColuna(proximaColuna.get());
            cardService.save(card);
            System.out.println("Card movido para a próxima coluna: " + proximaColuna.get().getNome());
        } else {
            System.out.println("Não é possível mover o card. Já está na última coluna ou coluna inválida.");
        }
    }

    public void cancelarCard(Long boardId) {
        Scanner scanner = new Scanner(System.in);

        Board board = encontrarBoard(boardId);

        // Listar todas as colunas e cards (exceto final e cancelamento)
        Set<Coluna> colunas = board.getColunas();
        List<Card> cards = new ArrayList<>();
        for (Coluna coluna : colunas) {
            if (coluna.getTipo() != TipoColuna.FINAL && coluna.getTipo() != TipoColuna.CANCELAMENTO) {
                Coluna colunaComCards = colunaService.findByIdWithCards(coluna.getId()).orElse(coluna);
                cards.addAll(colunaComCards.getCards());
            }
        }

        if (cards.isEmpty()) {
            System.out.println("Não há cards para cancelar.");
            return;
        }

        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            System.out.println((i + 1) + ". " + c.getTitulo() + " (Coluna: " + c.getColuna().getNome() + ")" + (c.isBloqueado() ? " [BLOQUEADO]" : ""));
        }

        System.out.print("Selecione o card para cancelar: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao < 1 || opcao > cards.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Card card = cards.get(opcao - 1);

        if (card.isBloqueado()) {
            System.out.println("Card está bloqueado e não pode ser cancelado.");
            return;
        }

        // Encontrar coluna de cancelamento
        Coluna colunaCancelamento = colunas.stream()
                .filter(c -> c.getTipo() == TipoColuna.CANCELAMENTO)
                .findFirst()
                .orElse(null);

        if (colunaCancelamento == null) {
            System.out.println("Coluna de cancelamento não encontrada.");
            return;
        }

        card.setColuna(colunaCancelamento);
        cardService.save(card);
        System.out.println("Card movido para a coluna de cancelamento!");
    }

    public void bloquearCard(Long boardId) {
        Scanner scanner = new Scanner(System.in);

        Board board = encontrarBoard(boardId);

        Set<Coluna> colunas = board.getColunas();
        List<Card> cards = new ArrayList<>();
        for (Coluna coluna : colunas) {
            if (coluna.getTipo() != TipoColuna.FINAL && coluna.getTipo() != TipoColuna.CANCELAMENTO) {
                Coluna colunaComCards = colunaService.findByIdWithCards(coluna.getId()).orElse(coluna);
                cards.addAll(colunaComCards.getCards());
            }
        }
        // Listar todos os cards não bloqueados
        List<Card> cardsDesbloqueados = cards.stream()
                .filter(c -> !c.isBloqueado())
                .toList();

        if (cardsDesbloqueados.isEmpty()) {
            System.out.println("Não há cards desbloqueados.");
            return;
        }

        for (int i = 0; i < cardsDesbloqueados.size(); i++) {
            Card c = cardsDesbloqueados.get(i);
            System.out.println((i + 1) + ". " + c.getTitulo() + " (Coluna: " + c.getColuna().getNome() + ")");
        }

        System.out.print("Selecione o card para bloquear: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao < 1 || opcao > cardsDesbloqueados.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Card card = cardsDesbloqueados.get(opcao - 1);

        System.out.print("Informe o motivo do bloqueio: ");
        String motivo = scanner.nextLine();

        card.setBloqueado(true);
        card.setMotivoBloqueio(motivo);
        cardService.save(card);
        System.out.println("Card bloqueado com sucesso!");
    }

    public void desbloquearCard(Long boardId) {
        Scanner scanner = new Scanner(System.in);

        Board board = encontrarBoard(boardId);
        Set<Coluna> colunas = board.getColunas();
        List<Card> cards = new ArrayList<>();
        for (Coluna coluna : colunas) {
            if (coluna.getTipo() != TipoColuna.FINAL && coluna.getTipo() != TipoColuna.CANCELAMENTO) {
                Coluna colunaComCards = colunaService.findByIdWithCards(coluna.getId()).orElse(coluna);
                cards.addAll(colunaComCards.getCards());
            }
        }
        // Listar todos os cards bloqueados
        List<Card> cardsBloqueados = cards.stream()
                .filter(Card::isBloqueado)
                .toList();

        if (cardsBloqueados.isEmpty()) {
            System.out.println("Não há cards bloqueados.");
            return;
        }

        for (int i = 0; i < cardsBloqueados.size(); i++) {
            Card c = cardsBloqueados.get(i);
            System.out.println((i + 1) + ". " + c.getTitulo() + " (Coluna: " + c.getColuna().getNome() + ")");
        }

        System.out.print("Selecione o card para desbloquear: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao < 1 || opcao > cardsBloqueados.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Card card = cardsBloqueados.get(opcao - 1);

        System.out.print("Informe o motivo do desbloqueio: ");
        String motivo = scanner.nextLine();

        card.setBloqueado(false);
        card.setMotivoDesbloqueio(motivo);
        cardService.save(card);
        System.out.println("Card desbloqueado com sucesso!");
    }

    public Board encontrarBoard(Long boardId){
        return boardService.findByIdWithColunas(boardId)
                .orElseThrow(() -> new RuntimeException("Board não encontrado"));
    }
}
