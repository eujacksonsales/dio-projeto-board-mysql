package dio.projeto.board;

import dio.projeto.board.menu.BoardMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleApp implements CommandLineRunner {

    private final BoardMenu boardMenu;

    public ConsoleApp(BoardMenu boardMenu){
        this.boardMenu = boardMenu;
    }

    @Override
    public void run(String... args) throws Exception {
        boardMenu.start();
    }
}
