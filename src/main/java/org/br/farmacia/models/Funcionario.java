package org.br.farmacia.models;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;

import java.sql.Date;

public class Funcionario {

    private String nome;
    private int id;
    private Date dataNascimento;
    private Genero genero;
    private Cargo cargo;
    private double salario;
    private Setor setor; // Mantemos Setor como um objeto separado, se for mais complexo

    // Atributos de Beneficios movidos para Funcionario
    private double valeRefeicao;
    private double valeAlimentacao;
    private double planoSaude;
    private double planoOdonto;

    public Funcionario(String nome,int id , Date dataNascimento , Genero genero,
                       Cargo cargo, double salario) {

        if (dataNascimento == null) throw new IllegalArgumentException("Idade inválida");
        if (salario <= 0) throw new IllegalArgumentException("Salário deve ser maior que zero");

        this.nome = nome;
        this.id = id;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.cargo = cargo;
        this.salario = salario;
        // Inicializa os benefícios com valores padrão (ou pode ser via outro construtor ou setters)
        this.valeRefeicao = 0.0;
        this.valeAlimentacao = 0.0;
        this.planoSaude = 0.0;
        this.planoOdonto = 0.0;
    }

    // Construtor adicional para incluir benefícios diretamente
    public Funcionario(String nome, int id, Date dataNascimento, Genero genero,
                       Cargo cargo, double salario, double valeRefeicao,
                       double valeAlimentacao, double planoSaude, double planoOdonto) {
        this(nome, id, dataNascimento, genero, cargo, salario); // Chama o construtor principal
        this.valeRefeicao = valeRefeicao;
        this.valeAlimentacao = valeAlimentacao;
        this.planoSaude = planoSaude;
        this.planoOdonto = planoOdonto;
    }

    // Getters e Setters para os atributos existentes de Funcionario
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setIdade(Date dataNascimento) { this.dataNascimento = dataNascimento; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    // Getters e Setters para os novos atributos de Beneficios
    public double getValeRefeicao() {
        return valeRefeicao;
    }

    public void setValeRefeicao(double valeRefeicao) {
        this.valeRefeicao = valeRefeicao;
    }

    public double getValeAlimentacao() {
        return valeAlimentacao;
    }

    public void setValeAlimentacao(double valeAlimentacao) {
        this.valeAlimentacao = valeAlimentacao;
    }

    public double getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(double planoSaude) {
        this.planoSaude = planoSaude;
    }

    public double getPlanoOdonto() {
        return planoOdonto;
    }

    public void setPlanoOdonto(double planoOdonto) {
        this.planoOdonto = planoOdonto;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", id=" + id +
                ", idade=" + dataNascimento +
                ", genero=" + genero +
                ", cargo=" + cargo +
                ", setor=" + (setor != null ? setor.getTipoSetor() : "Nenhum") +
                ", salario=R$" + String.format("%.2f", salario) +
                ", valeRefeicao=R$" + String.format("%.2f", valeRefeicao) +
                ", valeAlimentacao=R$" + String.format("%.2f", valeAlimentacao) +
                ", planoSaude=R$" + String.format("%.2f", planoSaude) +
                ", planoOdonto=R$" + String.format("%.2f", planoOdonto) +
                '}';
    }
}