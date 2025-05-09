package dio.projeto.board.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
    private boolean bloqueado;
    private String motivoBloqueio;
    private String motivoDesbloqueio;

    @ManyToOne
    private Coluna coluna;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getMotivoBloqueio() {
        return motivoBloqueio;
    }

    public void setMotivoBloqueio(String motivoBloqueio) {
        this.motivoBloqueio = motivoBloqueio;
    }

    public String getMotivoDesbloqueio() {
        return motivoDesbloqueio;
    }

    public void setMotivoDesbloqueio(String motivoDesbloqueio) {
        this.motivoDesbloqueio = motivoDesbloqueio;
    }

    public Coluna getColuna() {
        return coluna;
    }

    public void setColuna(Coluna coluna) {
        this.coluna = coluna;
    }
}
