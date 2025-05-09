CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE coluna (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    ordem INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    board_id BIGINT NOT NULL,
    FOREIGN KEY (board_id) REFERENCES board(id)
);

CREATE TABLE card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_criacao DATETIME NOT NULL,
    bloqueado BOOLEAN NOT NULL DEFAULT FALSE,
    motivo_bloqueio TEXT,
    motivo_desbloqueio TEXT,
    coluna_id BIGINT NOT NULL,
    FOREIGN KEY (coluna_id) REFERENCES coluna(id)
);